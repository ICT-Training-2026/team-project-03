package com.example.kintai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "department")
@Getter
@Setter
public class Department {

    @Id
    @Column(name = "DEPART_ID")
    private String departmentId;   // 部署ID → String に変更

    @Column(name = "DEPART_NAME", length = 50, nullable = false)
    private String departmentName;  // 部署名
}