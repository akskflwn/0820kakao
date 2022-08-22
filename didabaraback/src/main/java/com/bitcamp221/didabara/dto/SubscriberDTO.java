package com.bitcamp221.didabara.dto;

import com.bitcamp221.didabara.model.CategoryEntity;
import com.bitcamp221.didabara.model.SubscriberEntity;
import com.bitcamp221.didabara.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriberDTO {

  private Long id;
  private Long category;
  private Long user;
  private LocalDateTime createdDate;
  private LocalDateTime modifiedDate;

  public SubscriberDTO (SubscriberEntity subscriberEntity) {
    this.id = subscriberEntity.getId();
    this.category = subscriberEntity.getCategory();
    this.user = subscriberEntity.getUser();
    this.createdDate = subscriberEntity.getCreatedDate();
    this.modifiedDate = subscriberEntity.getModifiedDate();

  }

  public static SubscriberEntity toSubscriberEntity(final SubscriberDTO subscriberDTO) {

    return SubscriberEntity.builder()
            .id(subscriberDTO.getId())
            .category(subscriberDTO.getCategory())
            .user(subscriberDTO.getUser())
            .build();
  }
}
