package com.bitcamp221.didabara.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "emailconfig")
public class EmailConfigEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;


    @OneToOne(mappedBy = "emailConfigEntity")
    private UserEntity user;

    @Column(name = "auth_code", nullable = false, length = 30)
    private String authCode;
}
