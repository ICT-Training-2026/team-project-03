package com.example.demo.entity;

import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name="users")
public class User {
    @Id
    private String userId;
    private String password;
    private String name;
    private int departmentId;
    private String role; // "ADMIN" or "USER"
}