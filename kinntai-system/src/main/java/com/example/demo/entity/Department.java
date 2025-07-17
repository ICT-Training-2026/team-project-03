package com.example.demo.entity;

import jakarta.persistence.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "部署情報")
public class Department {

    @Id
    @Column(name = "DEPART_ID", length = 50)
    private String departId;

    @Column(name = "DEPART_NAME", length = 50)
    private String departName;
}