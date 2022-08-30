package com.bitcamp221.didabara.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "auth_code", nullable = false, length = 30)
    private String authCode;


}