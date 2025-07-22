package com.example.kintai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kintai.entity.Department;

// ★ @Repository は付けない
public interface DepartmentRepository extends JpaRepository<Department, String> {
    // 必要ならメソッドをここに追加
}