package com.bitcamp221.didabara.service;

import com.bitcamp221.didabara.mapper.EmailConfigMapper;
import com.bitcamp221.didabara.mapper.UserMapper;
import com.bitcamp221.didabara.model.EmailConfigEntity;
import com.bitcamp221.didabara.model.UserEntity;
import com.bitcamp221.didabara.presistence.EmailConfigRepository;
import com.bitcamp221.didabara.presistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;

@Slf4j
@Service
public class EmailConfigService {

    @Autowired
    private EmailConfigRepository emailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmailConfigMapper emailConfigMapper;

    private final JavaMailSender mailSender;

    /**
     * 작성자 : 김남주
     * 빨간줄 보이는게 맞습니다.
     *
     * @param mailSender
     */
    @Autowired
    public EmailConfigService(@Lazy JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    /**
     * 작성자 : 김남주
     * 메소드 기능 : 인증코드 받아서 체크하는 기능 (아직 구현 안됌)
     * 마지막 작성자 : 김남주
     *
     * @param emailAuthCodeMap // email, auth_code 필요
     * @return
     */
    public boolean checkEmail(Map emailAuthCodeMap) {
        Map haveAuthCodeUser = null;
        try {
            haveAuthCodeUser = userMapper.selectUsernameAndAuthCode(emailAuthCodeMap);
<<<<<<< HEAD
            log.info("checkEmailConfigEntity={}", haveAuthCodeUser);
            if (!haveAuthCodeUser.get("auth_code").equals(emailAuthCodeMap.get("authCode"))) {
=======
            log.info("haveAuthCodeUser.get(\"authCode\")={}", haveAuthCodeUser.get("authCode"));
            String o1 = (String) emailAuthCodeMap.get("authCode");
            String o = (String) haveAuthCodeUser.get("auth_code");
            log.info("o1={}", o1);
            log.info("o={}",o);
            if (!o1.equals(o)) {
>>>>>>> f742b18fcd014412e263c7ec9edb3ec7bb850e8b
                throw new Exception("일치하지 않는 계정, 코드");
            }
            return true;
        } catch (Exception e) {
            String message = e.getMessage();
            log.error("checkEmail={}", message);
            return false;
        }
    }


    /**
     * 작성자 : 김남주
     * 메서드 기능 : 회원가입 후 auth_code 전송 메서드
     * 마지막 작성자 : 김남주
     * 추가 : user 테이블의 pk 값이 emailconfig 테이블의 id 값이 같아야하는데
     * emailconfig 테이블의 id값 - 1 해야 user 테이블의 id값이 랑 맞음
     *
     * @param email
     * @throws Exception
     */
     public void mailsend(String email) throws Exception {
        // 난수 발생
        log.info("mailsend 실행2");
        String code = UUID.randomUUID().toString().substring(0, 6);
        log.info("uuid={}", code);

        // 이메일로 찾은 유저 객체
        UserEntity userIdByEmail = userMapper.selectUserIdByEmail(email);
<<<<<<< HEAD
        log.info("userIdByEmail.getUsername={}", userIdByEmail.getUsername());
=======
        log.info("userIdByEmail.getUsername={}",userIdByEmail.getUsername());
        log.info("userIdByEmail.getId()={}",userIdByEmail.getId());
>>>>>>> f742b18fcd014412e263c7ec9edb3ec7bb850e8b

        // emailconfig 테이블에 찾은 아이디 값,이메일,인증코드 저장

        int checkRow = 0;
        if (userIdByEmail != null) {
            checkRow= emailConfigMapper.updateUserIntoEmailconfig(userIdByEmail, code);
        }

        checkRow = emailConfigMapper.saveUserIntoEmailconfig(userIdByEmail, code);
        log.info("checkRow={}", checkRow);

        MimeMessage m = mailSender.createMimeMessage();
        MimeMessageHelper h = new MimeMessageHelper(m, "UTF-8");
        h.setFrom("kxg1198@naver.com");
        h.setTo(email);
        h.setSubject("인증 메일이 도착했습니다.");
        h.setText(code); // 이메일 본문에 적을 값

        mailSender.send(m);

    }



    public void mailsend22(String username) throws Exception {
         Map map = emailConfigMapper.findCode(username);
        System.out.println("///////////////////////////////////////");
        System.out.println(map.get("username"));
        System.out.println(map.get("auth_code"));
        System.out.println("///////////////////////////////////////");



        MimeMessage m = mailSender.createMimeMessage();
        MimeMessageHelper h = new MimeMessageHelper(m, "UTF-8");
        h.setFrom("kxg1198@naver.com");
        h.setTo((String)map.get("username"));
        h.setSubject("인증 메일이 도착했습니다.");
        h.setText((String) map.get("auth_code")); // 이메일 본문에 적을 값
        log.info("전송완료");
        mailSender.send(m);
    }


   public boolean check(Map map) throws Exception{
       System.out.println("///////////////////////////////////////");
       System.out.println(map.get("username"));
       System.out.println(map.get("auth_code"));
       System.out.println("///////////////////////////////////////");

       Map userANdCode = emailConfigMapper.findCode((String)map.get("username"));

        if(map.get("username").equals(userANdCode.get("username")) &&
           map.get("auth_code").equals(userANdCode.get("auth_code"))
        ){
             return true;

       }
           return false;

    }
}
