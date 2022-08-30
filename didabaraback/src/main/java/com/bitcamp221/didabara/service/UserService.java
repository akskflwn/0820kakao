package com.bitcamp221.didabara.service;

import com.bitcamp221.didabara.model.UserEntity;
import com.bitcamp221.didabara.model.UserInfoEntity;
import com.bitcamp221.didabara.presistence.UserInfoRepository;
import com.bitcamp221.didabara.presistence.UserRepository;
import com.bitcamp221.didabara.security.TokenProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    UserInfoRepository userInfoRepository;


    public UserEntity creat(final UserEntity userEntity) {
//    1. userEntity 유효성 검사.
        if (userEntity == null || userEntity.getUsername() == null) {
            throw new RuntimeException("invalid arguments");
        }

        final String username = userEntity.getUsername();
        final String nickname = userEntity.getNickname();

        //  2. 중복 검사
        if (userRepository.existsByUsername(username)) {
            log.warn("Username already exists {}", username);
            throw new RuntimeException("Username already exists");
        }
        // 3. 닉네임 중복검사
        if (userRepository.existsByNickname(nickname)) {
            log.warn("Nickname already exists {}", nickname);
            throw new RuntimeException("Nickname already exists");
        }
        return userRepository.save(userEntity);
    }

    //  아이디 & 비밀번호 일치 확인
    public UserEntity getByCredentials(final String username, final String password, final PasswordEncoder passwordEncoder) {
        final UserEntity originalUser = userRepository.findByUsername(username);

//    matches
        if (originalUser != null && passwordEncoder.matches(password, originalUser.getPassword())) {

            //로그인할떄 유저의 아이디랑 비빌번호가 일치할때
            //ban check을한다

            UserInfoEntity bancheck =userInfoRepository.findById(originalUser.getId()).orElseThrow(() ->
                    new IllegalArgumentException("해당 아이디가 없습니다."));

            if(!bancheck.isBan())
            {
                return originalUser;
            }
            else
            {
                log.warn("User {} hasbaned", username);
            }
        }

        return null;
    }

    //조회
    //username으로 조회하기
    public UserEntity findUser(final String username) {
        return userRepository.findByUsername(username);
    }

    //조회
    //userid로 조회하기
    public UserEntity findById(Long id) {
        //orElseThrow( )는 Optional 클래스에 포함된 메서드로,
        // Entity 조회와 예외 처리를 단 한 줄로 처리할 수 있음
        UserEntity user = userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 아이디가 없습니다."));
        return user;
    }

    //수정
    @Transactional
    public UserEntity update(Long id, Map map, PasswordEncoder passwordEncoder) {
        UserEntity findUser = userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 아이디가 없습니다."));


        findUser.changeNickname((String)map.get("nickname"));

        findUser.changePassword(passwordEncoder.encode((String)map.get("password")));

        UserEntity updatedUser=userRepository.save(findUser);
        return updatedUser;
    }

    //삭제

    public void deleteUser(Long id) {
        UserEntity findUser = userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 아이디가 없습니다"));
        userRepository.delete(findUser);


    }

    //에러 처리를 하는 이유는 프로그램이 에러로 인해 종료되지않게 하기 위함
    //유효성 검증
    private void validate(final UserEntity entity) {
        if (entity == null) {
            log.warn("Error: Entity can not be null");
            throw new RuntimeException("Entity cannot be null");
        }
        if (entity.getId() == null) {
            log.warn("Error: id can not be null");
            throw new RuntimeException("id cannot be null");
        }
    }


    public Map createKakaoUser(String token) {

        System.out.println("token = " + token);

        String reqURL = "https://kapi.kakao.com/v2/user/me";

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }


            System.out.println("response body : " + result);

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            Long id = element.getAsJsonObject().get("id").getAsLong();
            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            String nickname = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
            String profile_image = element.getAsJsonObject().get("properties").getAsJsonObject().get("profile_image").getAsString();
            String email = "";
            if (hasEmail) {
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }



            System.out.println("email : " + email);
            System.out.println("id : " + id);
            System.out.println("nickname = " + nickname);

            UserEntity user = UserEntity.builder()
                    .id(id)
                    .username(email)
                    .nickname(nickname)
                    .password(id + "")
                    .build();



            if (!userRepository.existsByUsername(email)) {

                UserEntity save = userRepository.save(user);

                System.out.println("///////////////////////////////////////////////////////////////////////////////");
                String code = UUID.randomUUID().toString().substring(0,6);
                String imagePath = profile_image;
                BufferedImage image = null;
                try {
                    image = ImageIO.read(new URL(profile_image));
                    String fileNm = imagePath.substring(imagePath.lastIndexOf("/") + 1);
                    System.out.println("///////////////////////////////////////////////////////////////////////////////");
                    log.info(fileNm);
                    System.out.println("///////////////////////////////////////////////////////////////////////////////");
                    File file = new File("C:\\발구지\\didabara\\didabaraback\\src\\main\\resources\\static\\images\\"+code+".jpg");

                    String fileUrl = "C:\\발구지\\didabara\\didabaraback\\src\\main\\resources\\static\\images\\";

                    ImageIO.write(image, "jpg", file);


                    UserInfoEntity userInfoEntity = UserInfoEntity.builder()
                            .id(save.getId())
                            .fileOriname("카카오톡프로필이미지")
                            .filename(code+".jpg")
                            .profileImageUrl(fileUrl)
                            .build();
                    userInfoRepository.save(userInfoEntity);

                }catch (Exception e){
                    log.info("오류");
                }
                System.out.println("///////////////////////////////////////////////////////////////////////////////");




                br.close();

            } else {
                log.info("이미 가입된 사용자입니다");
            }


            UserEntity findUser = userRepository.findByUsername(email);
            String find_user_token = tokenProvider.create(findUser);
            System.out.println("find_user_token = " + find_user_token);

            Map map = new HashMap();
            map.put(("token"),find_user_token);
            map.put(("id"),findUser.getId());
            map.put(("nickname"),findUser.getNickname());
            map.put(("username"),findUser.getUsername());


            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    /* 카카오 로그인(test) */
    public String[] getKaKaoAccessToken(String code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        String result = null;
        String id_token = null;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=4af7c95054f7e1d31cff647965678936"); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri=http://localhost:8080/auth/kakao"); // TODO 인가코드 받은 redirect_uri 입력
//            System.out.println("code = " + code);
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            // bearer 토큰 값만 추출(log에 찍히는 값의 이름은 id_Token)
            System.out.println("response body : " + result);
            String[] temp = result.split(",");
            id_token = temp[3].substring(11);
            System.out.println("idToken = " + id_token);


//            Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] arrTokens = new String[3];
        arrTokens[0] = access_Token;
        arrTokens[1] = refresh_Token;
        arrTokens[2] = id_token;

        // token 값들 배열로 리턴(프론트에서 쓰일지도 모르기 때문)
        return arrTokens;
    }

//    public UserEntity findUserByUserId(String userId){
//        UserEntity findUser=userRepository.findById((Long)userId).orElseThrow(()->
//                new IllegalArgumentException("해당 아이디가 없습니다"));
//        userRepository.save(findUser);
//    }


    /* 유저 저장 */
}