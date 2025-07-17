package com.example.attendance.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.attendance.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    // 出勤打刻
    @PostMapping("/clock-in")
    public String clockIn(@RequestParam Long userId) {
        return attendanceService.clockIn(userId);
    }

    // 退勤打刻
    @PostMapping("/clock-out")
    public String clockOut(@RequestParam Long userId) {
        return attendanceService.clockOut(userId);
    }
} 