package com.example.kintai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "permission")
@Getter
@Setter
public class Authority {

    @Id
    @Column(name = "ADMIN")
    private Integer authorityId;    // 権限ID (int型に変更)

    @Column(name = "ADMIN_NAME", length = 50, nullable = false)
    private String authorityName;   // 権限名
}