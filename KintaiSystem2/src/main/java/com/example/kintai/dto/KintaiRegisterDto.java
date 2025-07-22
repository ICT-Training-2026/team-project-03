package com.example.kintai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KintaiRegisterDto {
    private String userId;
    private String date;
    private String status;
    private String startTime;
    private String endTime;
}