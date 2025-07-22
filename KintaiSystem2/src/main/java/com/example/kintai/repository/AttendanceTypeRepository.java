package com.example.kintai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kintai.entity.AttendanceType;

// ★ @Repository は付けない
public interface AttendanceTypeRepository extends JpaRepository<AttendanceType, String> {
    // 必要ならメソッドをここに追加
}