package com.example.kintai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KintaiRegisterDto {
    // ユーザーID
    private String userId;

    // 勤怠日付（フォーマット: yyyy-MM-dd）
    private String date;

    // 勤怠区分（例: 出勤、遅刻、早退など）
    private String status;

    // 出勤時刻（フォーマット: HH:mm）
    private String startTime;

    // 退勤時刻（フォーマット: HH:mm）
    private String endTime;
}