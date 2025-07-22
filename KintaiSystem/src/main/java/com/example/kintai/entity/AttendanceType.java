package com.example.kintai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "work")
@Getter
@Setter
public class AttendanceType {

    @Id
    @Column(name = "ATT_ID")
    private Integer attendanceTypeId;   // 出勤区分ID (int型に変更)

    @Column(name = "ATT_NAME", length = 50, nullable = false)
    private String attendanceTypeName;  // 出勤区分名
}
