package com.example.kintai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "employee")
@Getter
@Setter
public class User {

    @Id
    @Column(name = "USER_ID", length = 50)
    private String userId;               // 社員ID

    @Column(name = "USER_NAME", length = 50, nullable = false)
    private String userName;             // 社員名

    @Column(name = "USER_YEAR")
    private Integer userYear;            // 入社年度

    @ManyToOne
    @JoinColumn(name = "DEPART_ID")
    private Department department;       // 部署 (FK int型)

    @ManyToOne
    @JoinColumn(name = "ADMIN")
    private Authority authority;         // 権限 (FK int型)

    @Column(name = "MAIL", length = 50, nullable = false)
    private String mail;                 // メールアドレス

    @Column(name = "PASS", length = 100, nullable = false)
    private String pass;                 // パスワード（ハッシュ化推奨）
}