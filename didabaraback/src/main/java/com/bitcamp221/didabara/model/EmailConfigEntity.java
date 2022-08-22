package com.bitcamp221.didabara.model;

import lombok.*;
<<<<<<< HEAD
=======

import javax.persistence.*;
import java.io.Serializable;

import lombok.*;
>>>>>>> f742b18fcd014412e263c7ec9edb3ec7bb850e8b

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

<<<<<<< HEAD

    @OneToOne(mappedBy = "emailConfigEntity")
    private UserEntity user;

=======
>>>>>>> f742b18fcd014412e263c7ec9edb3ec7bb850e8b
    @Column(name = "auth_code", nullable = false, length = 30)
    private String authCode;
}