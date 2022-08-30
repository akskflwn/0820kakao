package com.bitcamp221.didabara.controller;


import com.bitcamp221.didabara.model.UserInfoEntity;
import com.bitcamp221.didabara.presistence.UserInfoRepository;

import com.bitcamp221.didabara.service.UserInfoService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.util.Date;
import java.util.Map;


import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
@RestController
@RequestMapping(value="/userinfo",produces = APPLICATION_JSON_VALUE)
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserInfoRepository userInfoRepository;



    @GetMapping("/create")
    public ResponseEntity<String> createFeed(@AuthenticationPrincipal String id, @RequestParam("file") MultipartFile file) {
        // 시간과 originalFilename으로 매핑 시켜서 src 주소를 만들어 낸다.
        Date date = new Date();
        StringBuilder sb = new StringBuilder();

        // file image 가 없을 경우
        if (file.isEmpty()) {
            sb.append("none");
        } else {
            sb.append(date.getTime());
            sb.append(file.getOriginalFilename());
        }

        if (!file.isEmpty()) {
            File dest = new File("C://" + sb.toString());
            try {
                file.transferTo(dest);
                log.info("file={}", dest.toString());
            } catch (Exception e) {
                log.error("error={}", e.getMessage());
            }
            // db에 파일 위치랑 번호 등록
            UserInfoEntity userInfo = UserInfoEntity.builder()
                    .id(10L)
                    .profileImageUrl(file+"")
                    .build();
            userInfoRepository.save(userInfo);
        }
        return new ResponseEntity<String>("ok", HttpStatus.OK);
    }

    // 내 정보 보기
    @GetMapping
    public ResponseEntity<?> myPage(@AuthenticationPrincipal String id) {

        // id로 내 정보 찾아오기 (user 테이블 user_info 테이블 조인)
        Map byIdMyPage = userInfoService.findByIdMyPage(id);

        byIdMyPage.put("password", null);

        return ResponseEntity.ok().body(byIdMyPage);
    }

    /**
     *
     * @param id JWT id
     * @param map user_info 컬럼명들
     * @return
     */
    @PatchMapping
    public ResponseEntity<?> updateMyPage(@AuthenticationPrincipal String id, @RequestBody Map map) {
        try {
            userInfoService.updateMyPage(id,map);
            log.info("{}로 닉네임 변경완료",map.get("nickname"));
            log.info("비밀번호 변경완료 ");
            log.info("{}로 직업 변경완료 ",map.get("job"));
            
            return ResponseEntity.ok().body("변경완료");
        } catch (Exception e) {
            String msg = e.getMessage();
            log.error("error={}", msg);
            return ResponseEntity.badRequest().body(msg);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMypage(@AuthenticationPrincipal String id) {

        try {

            int delete = userInfoService.delete(id);
            return ResponseEntity.ok().body(delete);
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseEntity.badRequest().body(error);
        }

    }
//    @PostMapping("/upload")
//    public UserInfoEntity upload(@AuthenticationPrincipal String id, @RequestPart MultipartFile files) throws Exception {
//        Long userId= Long.valueOf(id);
//        String code = UUID.randomUUID().toString().substring(0,6);
//
//        String sourceFileName = files.getOriginalFilename();
//        String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase();
//        File destinationFile;
//        String destinationFileName;
//        String fileUrl = "C:\\발구지\\didabara\\didabaraback\\src\\main\\resources\\static\\images\\";
//        // mung-1은 자기 프로젝트이름으로 체인지!!
//
//        do {
//            destinationFileName = code+"." + sourceFileNameExtension;
//            destinationFile = new File(fileUrl + destinationFileName);
//        } while (destinationFile.exists());
//
//        destinationFile.getParentFile().mkdirs();
//        files.transferTo(destinationFile);
//
//        UserInfoEntity findUser=userInfoRepository.findById(userId).orElseThrow(() ->
//                new IllegalArgumentException("해당 아이디가 없습니다."));
//
//        findUser.setFilename(destinationFileName);
//        findUser.setProfileImageUrl(fileUrl);
//        findUser.setFileOriname(sourceFileName);
//
//        userInfoRepository.save(findUser);
//        return findUser;
//
//    }


}