package com.bitcamp221.didabara.dto;

import com.bitcamp221.didabara.model.CategoryEntity;
import com.bitcamp221.didabara.model.ReportEntity;
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
public class ReportDTO {

  private Long id;
  private Long user;
  private Long host;
  private String content;
  private String reportCategory;
  private LocalDateTime createdDate;
  private LocalDateTime modifiedDate;

  public ReportDTO (ReportEntity reportEntity) {
    this.id = reportEntity.getId();
    this.user = reportEntity.getUser();
    this.host = reportEntity.getHost();
    this.content = reportEntity.getContent();
    this.reportCategory = reportEntity.getReportCategory();
    this.createdDate = reportEntity.getCreatedDate();
    this.modifiedDate = reportEntity.getModifiedDate();
  }

  public static ReportEntity toReportEntity(final ReportDTO reportDTO) {

    return ReportEntity.builder()
            .id(reportDTO.getId())
            .user(reportDTO.getUser())
            .host(reportDTO.getHost())
            .content(reportDTO.getContent())
            .reportCategory(reportDTO.getReportCategory())
            .build();
  }

}
