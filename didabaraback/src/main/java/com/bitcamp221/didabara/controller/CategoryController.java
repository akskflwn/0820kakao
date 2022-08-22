
package com.bitcamp221.didabara.controller;

import com.bitcamp221.didabara.mapper.CategoryMapper;
import com.bitcamp221.didabara.model.CategoryEntity;
import com.bitcamp221.didabara.model.UserEntity;
import com.bitcamp221.didabara.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
<<<<<<< HEAD
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
>>>>>>> f742b18fcd014412e263c7ec9edb3ec7bb850e8b

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

<<<<<<< HEAD
    @GetMapping
    String confirm(@AuthenticationPrincipal String username){


        log.info(username);

        return "접속됨"+username;
    }



}
=======
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;

    @GetMapping("/kakao")
    public ResponseEntity<?> kakao(@AuthenticationPrincipal String userId) {
        log.info("username={}", userId);

        if (userId == null) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok().body(userId);
    }

    /**
     * 작성자 : 김남주
     * 메서드 기능 : 내가 만든방 보여주는 기능
     * 마지막 수정자 : 김남주
     * @param username // user token 값 확인
     * @param userId  // email 값 -> id로 변경해야 할듯
     * @param currentPage 현재 페이지
     * @return List
     */
    @GetMapping("/myroom/{userId}")
    public ResponseEntity<?> myroom(@AuthenticationPrincipal String username,
                                    @PathVariable String userId,
                                    @RequestParam(defaultValue = "1") int currentPage)
    {
        log.info("username={}",username);
        List<Map<UserEntity,CategoryEntity>> maps = categoryMapper.selectMyRoomAndPage(userId, (currentPage - 1) * 10);
        return ResponseEntity.ok().body(maps);

    }
}

>>>>>>> f742b18fcd014412e263c7ec9edb3ec7bb850e8b
