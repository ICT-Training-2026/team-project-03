package com.example.demo.entity;

import jakarta.persistence.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "社員情報")
public class User {

    @Id
    @Column(name = "USER_ID", length = 50, nullable = false)
    private String userId;

    @Column(name = "USER_NAME", length = 50, nullable = false)
    private String name;

    @Column(name = "USER_YEAR", length = 4)
    private Integer userYear;

    @Column(name = "DEPART_ID", length = 50)
    private String departmentId;

    @Column(name = "DEPART_NAME", length = 50)
    private String departmentName;

    @Column(name = "MAIL", length = 50)
    private String mail;

    @Column(name = "ADMIN", length = 10)
    private String admin;

    @Column(name = "PASS", length = 50, nullable = false)
    private String pass;
}