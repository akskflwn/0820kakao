package com.bitcamp221.didabara.dto;

import com.bitcamp221.didabara.model.EmailConfigEntity;
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
public class EmailConfigDTO {

  private Long id;
  private Long user;
  private String authCode;
  private LocalDateTime createdDate;
  private LocalDateTime modifiedDate;

  public EmailConfigDTO (EmailConfigEntity emailConfigEntity) {
    this.id = emailConfigEntity.getId();
    this.authCode = emailConfigEntity.getAuthCode();
    this.createdDate = emailConfigEntity.getCreatedDate();
    this.modifiedDate = emailConfigEntity.getModifiedDate();
  }

  public static EmailConfigEntity toEmailConfigEntity(final EmailConfigDTO emailConfigDTO) {

    return EmailConfigEntity.builder()
            .id(emailConfigDTO.getId())
            .authCode(emailConfigDTO.getAuthCode())
            .build();
  }
}
