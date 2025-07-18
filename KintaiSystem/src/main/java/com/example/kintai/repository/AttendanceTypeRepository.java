package com.example.kintai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kintai.entity.AttendanceType;

public interface AttendanceTypeRepository extends JpaRepository<AttendanceType, String> {
}