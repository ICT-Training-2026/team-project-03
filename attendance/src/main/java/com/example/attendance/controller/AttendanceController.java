package com.example.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.attendance.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService; 

    // 出勤打刻
    @PostMapping("/clock-in")
    public String clockIn(@RequestParam Long userId) {//haitoテスト
        return attendanceService.clockIn(userId);
    }

    // 退勤打刻
    @PostMapping("/clock-out")
    public String clockOut(@RequestParam Long userId) {
        return attendanceService.clockOut(userId);
    }
} 