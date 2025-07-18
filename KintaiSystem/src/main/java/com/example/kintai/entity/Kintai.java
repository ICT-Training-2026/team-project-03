package com.example.kintai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "attendance")
@Getter
@Setter
public class Kintai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // 勤怠テーブル用の主キー（auto increment用）

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;                     // 社員 (FK)

    @ManyToOne
    @JoinColumn(name = "DEPART_ID")
    private Department department;         // 部署 (FK int型)

    @ManyToOne
    @JoinColumn(name = "ATT_ID")
    private AttendanceType attendanceType; // 出勤区分 (FK int型)

    @Column(name = "DATE", nullable = false)
    private java.sql.Date date;            // 日付

    @Column(name = "IN_TIME_H")
    private String inTimeH;                // 出勤時刻(時)

    @Column(name = "OUT_TIME_H")
    private String outTimeH;               // 退勤時刻(時)

    @Column(name = "IN_TIME_M")
    private String inTimeM;                // 出勤時刻(分)

    @Column(name = "OUT_TIME_M")
    private String outTimeM;               // 退勤時刻(分)

    @Column(name = "YEAR")
    private Integer year;                  // 年

    @Column(name = "MONTH")
    private Integer month;                 // 月

    @Column(name = "DAY")
    private Integer day;                   // 日
}