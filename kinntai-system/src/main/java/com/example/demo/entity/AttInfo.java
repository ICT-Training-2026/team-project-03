package com.example.demo.entity;

import jakarta.persistence.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "出勤区分情報")
public class AttInfo {

    @Id
    @Column(name = "ATT_ID", length = 50)
    private String attId;

    @Column(name = "ATT_NAME", length = 50)
    private String attName;
}