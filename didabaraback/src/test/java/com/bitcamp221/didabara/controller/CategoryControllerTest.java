package com.bitcamp221.didabara.controller;

import com.bitcamp221.didabara.model.CategoryEntity;
import com.bitcamp221.didabara.presistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryControllerTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void saveUser() {
        for (int i = 2; i < 100; i++) {
            CategoryEntity category = CategoryEntity.builder()
                    .id((long) i)
                    .content("content" + i)
                    .host((long) i)
                    .inviteCode("invite" + i)
                    .title("title" + i)
                    .profileImageUrl("url" + i)
                    .build();
            categoryRepository.save(category);
        }

    }
}