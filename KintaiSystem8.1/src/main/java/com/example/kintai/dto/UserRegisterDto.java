// src/main/java/com/example/kintai/dto/UserRegisterDto.java
package com.example.kintai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDto {
    private String userId;
    private String userName;
    private String pass;
    private String mail;
    private String departId;   // DepartmentRepository で検索するキー
    private String hireDate;   // "YYYY-MM-DD" 形式
    private Integer admin;     // 0 または 1
}