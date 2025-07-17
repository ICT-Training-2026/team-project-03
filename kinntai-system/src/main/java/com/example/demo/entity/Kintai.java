package com.example.demo.entity;

import jakarta.persistence.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "勤怠情報")
public class Kintai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
<<<<<<< HEAD
    private Long userId;
    private String kintaiKubun; // 出勤 or 退勤
    private LocalDateTime time;
=======

    @Column(name = "DATE")
    private String date;

    @Column(name = "USER_ID", length = 50)
    private String userId;

    @Column(name = "DEPART_ID", length = 50)
    private String departId;

    @Column(name = "IN_TIME_H", length = 10)
    private String inTimeH;

    @Column(name = "OUT_TIME_H", length = 10)
    private String outTimeH;

    @Column(name = "ATT_ID", length = 10)
    private String attId;

    @Column(name = "IN_TIME_M", length = 20)
    private String inTimeM;

    @Column(name = "OUT_TIME_M", length = 20)
    private String outTimeM;

    @Column(name = "YEAR", length = 20)
    private String year;

    @Column(name = "MONTH", length = 20)
    private String month;

    @Column(name = "DAY", length = 20)
    private String day;
>>>>>>> refs/remotes/origin/master
}