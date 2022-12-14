package com.bitcamp221.didabara.dto;

import com.bitcamp221.didabara.model.CategoryEntity;
import com.bitcamp221.didabara.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

  private Long id;
  private Long host;
  private String title;
  private String content;
  private String inviteCode;
  private String profileImageUrl;
  private LocalDateTime createdDate;
  private LocalDateTime modifiedDate;

  public CategoryDTO(final CategoryEntity categoryEntity) {
    this.id = categoryEntity.getId();
    this.host = categoryEntity.getHost();
    this.title = categoryEntity.getTitle();
    this.content = categoryEntity.getContent();
    this.inviteCode = categoryEntity.getInviteCode();
    this.createdDate = categoryEntity.getCreatedDate();
    this.modifiedDate = categoryEntity.getModifiedDate();
  }

  public static CategoryEntity toCategoryEntity(final CategoryDTO categoryDTO) {

    return CategoryEntity.builder()
            .id(categoryDTO.getId())
            .host(categoryDTO.getHost())
            .title(categoryDTO.getTitle())
            .content(categoryDTO.getContent())
            .inviteCode(categoryDTO.getInviteCode())
            .profileImageUrl(categoryDTO.getProfileImageUrl())
            .build();
  }
}
