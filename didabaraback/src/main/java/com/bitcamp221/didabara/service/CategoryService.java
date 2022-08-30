package com.bitcamp221.didabara.service;

import com.bitcamp221.didabara.model.CategoryEntity;
import com.bitcamp221.didabara.presistence.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class CategoryService {

  @Autowired
  private CategoryRepository categoryRepository;



  //  -----------------------------------------------------
//  작성자 : 문병훈
//  메소드 정보 : DB로부터 받아온 Entity에 대해서 사전 검사
//  마지막 수정자 : 문병훈
//  -----------------------------------------------------
  private void validate(final CategoryEntity categoryEntity) {
    if (categoryEntity == null) {
      log.warn("Error : categoryEntity is null");
      throw new RuntimeException("categoryEntity is null");
    }
  }


  //  ---------------------------------------------------
//  작성자 : 문병훈
//  메소드 정보 : Category 생성
//  마지막 수정자 : 문병훈
//  -----------------------------------------------------
  public CategoryEntity create(final CategoryEntity categoryEntity) {
//    카테고리 유효성 검사 진행
    validate(categoryEntity);

    categoryRepository.save(categoryEntity);

    log.info("categoty entity create success : {}", categoryEntity.getId());

    return categoryRepository.findCategory(categoryEntity.getId());
  }

  //  ---------------------------------------------------
//  작성자 : 문병훈
//  메소드 정보 : category의 id 값으로 특정 Category 정보 출력
//  마지막 수정자 : 문병훈
//  -----------------------------------------------------
  public CategoryEntity findByCategory(final Long categoryId) {

    return categoryRepository.findCategory(categoryId);
  }

  //  ---------------------------------------------------
//  작성자 : 문병훈
//  메소드 정보 : user본인이 생성한 Category 전체 출력
//  마지막 수정자 : 문병훈
//  -----------------------------------------------------
  public List<CategoryEntity> myList(final Long userId) {

    return categoryRepository.findByMyList(userId);
  }

  //  ---------------------------------------------------
//  작성자 : 문병훈
//  메소드 정보 : 특정 Category 수정
//  마지막 수정자 : 문병훈
//  -----------------------------------------------------
  public CategoryEntity update(final CategoryEntity categoryEntity) {
    validate(categoryEntity);

    final Optional<CategoryEntity> original = categoryRepository.findById(categoryEntity.getId());

    log.info("오리지날{}", original);

    original.ifPresent(entity -> {

      entity.changeEntity(categoryEntity);

      categoryRepository.save(entity);
    });

    log.info("category service update success : {}", categoryEntity.getId());

    return findByCategory(categoryEntity.getId());
  }

  //  ---------------------------------------------------
//  작성자 : 문병훈
//  메소드 정보 : 특정 Category 삭제
//  마지막 수정자 : 문병훈
//  -----------------------------------------------------
  public List<CategoryEntity> delete(final Long host, final CategoryEntity categoryEntity) {

    validate(categoryEntity);

    try {

      categoryRepository.delete(categoryEntity);

      log.info("category service delete success : {}", host);

      return myList(host);
    } catch (Exception e) {
      log.error("Category Delete Failed");

      throw new RuntimeException("Category Delete Failed" + categoryEntity.getId());
    }
  }

  public boolean existByCategory (final Long categoryId) {
    try {
      log.info("exist join success");

      return categoryRepository.findById(categoryId).isEmpty();
    } catch (Exception e){
      log.info("exist join failed");

      return false;
    }
  }
}
