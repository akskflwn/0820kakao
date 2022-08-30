package com.bitcamp221.didabara.service;

import com.bitcamp221.didabara.mapper.UserInfoMapper;
import com.bitcamp221.didabara.mapper.UserMapper;
import com.bitcamp221.didabara.model.UserEntity;
import com.bitcamp221.didabara.model.UserInfoEntity;
import com.bitcamp221.didabara.presistence.UserInfoRepository;
import com.bitcamp221.didabara.presistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private  UserRepository userRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 작성자 : 김남주
     * 메서드 기능 : 내 정보 보기
     * 마지막 작성자 : 김남주
     * @param id
     * @return Map
     */
    public Map findByIdMyPage(String id) {

        Map byMap = userInfoMapper.findByMap(id);

        return byMap;
    }

    public int updateByIdMyPage(String username, Map map) throws Exception {

        // nickname 중복검사
        UserEntity checkNickname = userInfoMapper.checkNickname(map);
        if (checkNickname != null) {
            throw new Exception("nickname이 중복입니다.");
        }

        // 패스워드 encode
        String encodePassword = passwordEncoder.encode((String) map.get("password"));
        map.put("password", encodePassword);


        int checkRow = userInfoMapper.updateUserInfo(username, map);
        log.info("checkRow={}",checkRow);

        if (checkRow != 0){
            return checkRow;
        } else {
            throw new Exception("업데이트 실패");
        }

    }

    public int delete(String id) throws Exception {
        int checkRow = userInfoMapper.deleteUserAndInfo(id);
        log.info("checkRow={}",checkRow);
        if (checkRow != 0) {
            return checkRow;
        } else {
            throw new Exception("삭제 실패");
        }
    }

    public Map updateMyPage(String id, Map map) {

        Long userId = Long.valueOf(id);


        UserEntity findUserEntity =
                userRepository.findById(userId).orElseThrow(()->
                        new IllegalArgumentException("해당 아이디가없습니다"));
        UserInfoEntity findUserInfo=
                userInfoRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("해당 유저 정보가 없습니다"));

        if(map.get("nickname")!=null) {
            findUserEntity.changeNickname((String) map.get("nickname"));
        }
        if(map.get("password")!=null) {
            findUserEntity.changePassword(passwordEncoder.encode((String)map.get("password")));
        }
        if(map.get("job")!=null) {
            findUserInfo.setJob((String) map.get("job"));
        }
        userInfoRepository.save(findUserInfo);
        userRepository.save(findUserEntity);


        return map;

    }
}