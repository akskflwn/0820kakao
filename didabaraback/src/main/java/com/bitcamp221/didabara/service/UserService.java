package com.bitcamp221.didabara.service;

import com.bitcamp221.didabara.dto.UserDTO;
import com.bitcamp221.didabara.model.UserEntity;
import com.bitcamp221.didabara.presistence.UserRepository;
import com.bitcamp221.didabara.security.TokenProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;


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
            return originalUser;
        }

        return null;
    }

    //조회
    //username으로 조회하기
    public UserEntity findUser(final String username){
        return userRepository.findByUsername(username);
    }
    //조회
    //userid로 조회하기
    public UserEntity findById(Long id){
        //orElseThrow( )는 Optional 클래스에 포함된 메서드로,
        // Entity 조회와 예외 처리를 단 한 줄로 처리할 수 있음
        UserEntity user = userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 아이디가 없습니다."));
        return user;
    }

    //수정
    @Transactional
    public UserEntity update(UserEntity userEntity){
        UserEntity user = userRepository.findById(userEntity.getId()).orElseThrow(() ->
                new IllegalArgumentException("해당 아이디가 없습니다."));

        user.changeNickname(userEntity.getNickname());

        user.changePassword(userEntity.getPassword());

        userRepository.save(user);
        return user;
    }

    //삭제

    public void deleteUser(Long id){
        UserEntity findUser=userRepository.findById(id).orElseThrow(()->
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


    public UserEntity createKakaoUser(String token)  {

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
            String nickname= element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            String email = "";
            if(hasEmail){
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();

            }
            System.out.println("id : " + id);
            System.out.println("email : " + email);

            UserEntity kakaoUser = UserEntity.builder()
                    .id(id)
                    .username(email)
                    .nickname(nickname)
                    .password(id+"")
                    .build();

            final String tokenasd=tokenProvider.create(kakaoUser);
            System.out.println("///////////////////////////////////////////////////////");
            System.out.println(tokenasd);
            System.out.println("///////////////////////////////////////////////////////");
            userRepository.save(kakaoUser);

            br.close();
            return kakaoUser;




        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* 카카오 로그인(test) */
    public String getKaKaoAccessToken(String code){
        String access_Token="";
        String refresh_Token ="";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try{
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
            System.out.println("code = " + code);
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

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


            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return access_Token;
    }



    /* 유저 저장 */
}