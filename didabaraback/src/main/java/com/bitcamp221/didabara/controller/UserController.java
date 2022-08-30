package com.bitcamp221.didabara.controller;


import com.bitcamp221.didabara.model.UserEntity;

import com.bitcamp221.didabara.model.UserInfoEntity;
import com.bitcamp221.didabara.presistence.EmailConfigRepository;
import com.bitcamp221.didabara.presistence.UserInfoRepository;
import com.bitcamp221.didabara.presistence.UserRepository;
import com.bitcamp221.didabara.service.UserService;
import com.sun.net.httpserver.Authenticator;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.bitcamp221.didabara.dto.ResponseDTO;
import com.bitcamp221.didabara.dto.UserDTO;
import com.bitcamp221.didabara.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("auth")
public class UserController {

    @Autowired
    private UserService userService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    UserInfoRepository userInfoRepository;

    //  회원가입
//  http://localhost:8080/auth/signup
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        log.info("회원가입 시작");
        try {
//      받은 데이터 유효성 검사
            if (userDTO == null || userDTO.getPassword() == null) {
                throw new RuntimeException("Invalid Password value");
            }


            UserEntity userEntity = UserEntity.builder()
                    .username(userDTO.getUsername())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .nickname(userDTO.getNickname())
                    .build();

            UserEntity registeredUser = userService.creat(userEntity);

            ///////////////////////////////////UserInfoEntity Save///////////////////////////
            UserInfoEntity userInfoEntity = UserInfoEntity.builder()
                    .id(registeredUser.getId())
                    .filename("default.jpg")
                    .profileImageUrl("C:\\발구지\\didabara\\didabaraback\\src\\main\\resources\\static\\images")
                    .fileOriname("default.jpg")
                    .build();


            userInfoRepository.save(userInfoEntity);


            UserDTO responseUserDTO = UserDTO.builder()
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    .nickname(registeredUser.getNickname())
                    .build();

            log.info("회원가입 완료");

            return ResponseEntity.ok().body(responseUserDTO);

        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();

            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    //  로그인
//  http://localhost:8080/auth/signin
    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        
        UserEntity user = userService.getByCredentials(
                userDTO.getUsername(),
                userDTO.getPassword(),
                passwordEncoder
        );

        if (user != null) {
//    토큰 생성.
            final String token = tokenProvider.create(user);

            final UserDTO responsUserDTO = UserDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .nickname(user.getNickname())
                    .token(token)
                    .build();

            return ResponseEntity.ok().body(responsUserDTO);
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login Failed")
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    //조회
    @GetMapping("/user")
    public UserEntity findbyId(@AuthenticationPrincipal String id) {
        Long userId = Long.valueOf(id);
        return userService.findById(userId);
    }

    //수정
    //patch --> 엔티티의 일부만 업데이트하고싶을때
    //put --> 엔티티의 전체를 변경할떄
    //put 을 사용하면 전달한값 외는 모두 null or 초기값으로 처리된다고함..
    @PatchMapping("/user")
    public ResponseEntity<?> update(@AuthenticationPrincipal String id,@RequestBody Map map) {

        Long userId = Long.valueOf(id);
        try {
            userService.update(userId, map, passwordEncoder);
            return ResponseEntity.ok().body("업데이트 성공");

        }catch (Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //삭제
    @DeleteMapping("/user")
    public ResponseEntity<?> deletUser(@AuthenticationPrincipal String id) {
        try {

            Long userId = Long.valueOf(id);
            userService.deleteUser(userId);
            return ResponseEntity.ok().body("삭제 성공");

        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            log.error("삭제 실패");
            return ResponseEntity.badRequest().body(responseDTO);

        }
    }

    //프론트에서 인가코드 받아오는 url
    /* 카카오 로그인 */
    @GetMapping("/kakao")
    public ResponseEntity<?> kakaoCallback(@Param("code") String code) {
        try {
            log.info("code={}", code);

            String[] access_Token = userService.getKaKaoAccessToken(code);
            String access_found_in_token = access_Token[0];
            // 배열로 받은 토큰들의 accsess_token만 createKaKaoUser 메서드로 전달
            Map map = userService.createKakaoUser(access_found_in_token);
            log.info(map.toString());

            UserDTO kakaoUser = UserDTO.builder()
                    .token((String)map.get("token"))
                            .id((Long)map.get("id"))
                                    .nickname((String)map.get("nickname"))
                                            .username((String) map.get("username"))
                                                    .build();

            log.info("유저 리턴완료");
            return ResponseEntity.ok().body(kakaoUser);

    } catch (Exception e) {
        ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
        log.error("업데이트 실패");

        return ResponseEntity.badRequest().body(responseDTO);
    }



    }

    // https://kauth.kakao.com/oauth/authorize?client_id=4af7c95054f7e1d31cff647965678936&redirect_uri=http://localhost:8080/auth/kakao&response_type=code

}
