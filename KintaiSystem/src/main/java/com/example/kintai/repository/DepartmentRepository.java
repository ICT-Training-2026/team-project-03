package com.example.kintai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kintai.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, String> {
    // 特殊な検索が必要ならここにメソッド追加
}