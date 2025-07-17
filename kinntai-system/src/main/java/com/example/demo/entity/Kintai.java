package com.example.demo.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Kintai {
    private Long id;
    private Long userId;
    private String kintaiKubun; // 出勤 or 退勤
    private LocalDateTime time;
}
