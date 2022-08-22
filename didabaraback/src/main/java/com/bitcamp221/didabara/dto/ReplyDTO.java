package com.bitcamp221.didabara.dto;

import com.bitcamp221.didabara.model.CategoryItemEntity;
import com.bitcamp221.didabara.model.ReplyEntity;
import com.bitcamp221.didabara.model.SubscriberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {

  private Long id;
  private Long categoryItem;
  private Long writer;
  private String content;
  private LocalDateTime createdDate;
  private LocalDateTime modifiedDate;

  public ReplyDTO (ReplyEntity replyEntity) {
    this.id = replyEntity.getId();
    this.categoryItem = replyEntity.getCategoryItem();
    this.writer = replyEntity.getWriter();
    this.content = replyEntity.getContent();
    this.createdDate = replyEntity.getCreatedDate();
    this.modifiedDate = replyEntity.getModifiedDate();
  }

  public static ReplyEntity toReplyEntity(final ReplyDTO replyDTO) {

    return ReplyEntity.builder()
            .id(replyDTO.getId())
            .categoryItem(replyDTO.getCategoryItem())
            .writer(replyDTO.getWriter())
            .content(replyDTO.getContent())
            .build();
  }
}
