package com.bitcamp221.didabara.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_info")
public class UserInfoEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Long id;

  @Column(name = "job")
  private String job;

  //  0은 일반 유저, 1은 관리자
  @Column(name = "role")
  @ColumnDefault("0")
  private int role;

  @Column(name = "ban")
  @ColumnDefault("false")
  private boolean ban;

  //  프로필 사진 컬럼
  @Column(name = "profile_image_url")
  //  @ColumnDefault("기본 프로필 제공 이미지 경로")
  private String profileImageUrl;

  @Column(name="file_name")
  private String filename;

  @Column(name="file_ori_name")
  private String fileOriname;

  public void setId(Long id) {
    this.id = id;
  }

  public void setJob(String job) {
    this.job = job;
  }

  public void setRole(int role) {
    this.role = role;
  }

  public void setBan(boolean ban) {
    this.ban = ban;
  }

  public void setProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public void setFileOriname(String fileOriname) {
    this.fileOriname = fileOriname;
  }
}