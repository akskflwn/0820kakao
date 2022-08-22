package com.bitcamp221.didabara.controller;


import com.bitcamp221.didabara.dto.EmailConfigDTO;
import com.bitcamp221.didabara.model.EmailConfigEntity;
import com.bitcamp221.didabara.model.UserEntity;
import com.bitcamp221.didabara.presistence.EmailConfigRepository;
import com.bitcamp221.didabara.presistence.UserRepository;
import com.bitcamp221.didabara.service.EmailConfigService;
import com.bitcamp221.didabara.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.bitcamp221.didabara.dto.ResponseDTO;
import com.bitcamp221.didabara.dto.UserDTO;
import com.bitcamp221.didabara.model.UserEntity;
import com.bitcamp221.didabara.security.TokenProvider;
import com.bitcamp221.didabara.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("auth")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private EmailConfigRepository emailConfigRepository;

    //  회원가입
//  http://localhost:8080/auth/signup
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
//      받은 데이터 유효성 검사
            if (userDTO == null || userDTO.getPassword() == null) {
                throw new RuntimeException("Invalid Password value");
            }
        //난수생성
            String code = UUID.randomUUID().toString().substring(0, 6);
            log.info(code);

        //
            EmailConfigEntity emailConfigEntity = EmailConfigEntity.builder()
                    .authCode(code)
                    .build();
            emailConfigRepository.save(emailConfigEntity);


        //
//      요청을 이용해 저장할 유저 객체 생성
            UserEntity userEntity = UserEntity.builder()
                    .username(userDTO.getUsername())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .nickname(userDTO.getNickname())
                    .emailConfigEntity(emailConfigEntity)
                    .build();




//      서비스를 이용해 리포지터리에 유저 저장


            UserEntity registeredUser = userService.creat(userEntity);





//
           System.out.println("registerdUser Datetiem:" + registeredUser.getCreatedDate());
           System.out.println("registerdUser Modifiedtime:" + registeredUser.getModifiedDate());

      //응답객체 만들기(패스워드 제외)
            UserDTO responseUserDTO = UserDTO.builder()
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    .nickname(registeredUser.getNickname())
                    .build();

      //유저 정보는 현재 하나이므로 리스트로 만들 필요 없음
      //ResponseDTO를 사용하지 않고 UserDTO 타입으로 반환
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
            //추가해야할거
            //유저타입으로 카카오유저랑 유저랑 구별해서
            //카카오유저 로그인할때마다 토큰 생성해주기?
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
    // url로 접근할떄 토큰을 확인한다던가 보안성 로직이 필요할듯함?
    @GetMapping("/user/{id}")
    public UserEntity findbyId(@PathVariable Long id){
        return userService.findById(id);
    }

    //수정
    //patch --> 엔티티의 일부만 업데이트하고싶을때
    //put --> 엔티티의 전체를 변경할떄
    //put 을 사용하면 전달한값 외는 모두 null or 초기값으로 처리된다고함..
    @PatchMapping("/user")
    public ResponseEntity<?> update(@RequestBody UserDTO userDTO){

        try {
            UserEntity userEntity = UserEntity.builder()
                    .id(userDTO.getId())
                    .nickname(userDTO.getNickname())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .build();
            UserEntity updatedUser = userService.update(userEntity);

            UserDTO ResponseUserDTO = UserDTO.builder()
                    .id(updatedUser.getId())
                    .nickname(updatedUser.getNickname())
                    .build();
            log.info("업데이트 완료");

            return ResponseEntity.ok().body(ResponseUserDTO);
        }

        catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            log.error("업데이트 실패");
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }
    //삭제
    @DeleteMapping("/user")
    public void deletUser(@RequestBody UserDTO userDTO){
        userService.deleteUser(userDTO.getId());
        log.info("삭제완료");
    }


    //프론트에서 인가코드 받아오는 url
    /* 카카오 로그인 */
    @GetMapping("/kakao")
    public ResponseEntity<?> kakaoCallback(String code) {
        log.info("code={}", code);

        String access_Token = userService.getKaKaoAccessToken(code);

        UserEntity kakaoUser=userService.createKakaoUser(access_Token);
        //tokenProvider.create(kakaoUser);


        return ResponseEntity.ok().body(access_Token);
    }

    // https://kauth.kakao.com/oauth/authorize?client_id=4af7c95054f7e1d31cff647965678936&redirect_uri=http://localhost:8080/auth/kakao&response_type=code


}

