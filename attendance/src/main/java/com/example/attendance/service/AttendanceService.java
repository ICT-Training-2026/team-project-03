package com.example.attendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.UserRepository;
import com.example.attendance.model.AttendanceRecord;
import com.example.attendance.model.User;
import java.time.LocalDateTime;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private UserRepository userRepository;

    public String clockIn(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return "ユーザーが見つかりません";
        AttendanceRecord record = new AttendanceRecord();
        record.setUser(user);
        record.setClockInTime(LocalDateTime.now());
        attendanceRepository.save(record);
        return "出勤打刻しました";
    }

    public String clockOut(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return "ユーザーが見つかりません";
        AttendanceRecord record = attendanceRepository.findByUser(user)
            .stream()
            .filter(r -> r.getClockOutTime() == null)
            .findFirst()
            .orElse(null);
        if (record == null) return "出勤記録が見つかりません";
        record.setClockOutTime(LocalDateTime.now());
        attendanceRepository.save(record);
        return "退勤打刻しました";
    }
} 