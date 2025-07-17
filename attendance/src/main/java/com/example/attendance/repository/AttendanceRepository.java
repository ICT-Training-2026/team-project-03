package com.example.attendance.repository;

import com.example.attendance.model.AttendanceRecord;
import com.example.attendance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByUser(User user);
} 