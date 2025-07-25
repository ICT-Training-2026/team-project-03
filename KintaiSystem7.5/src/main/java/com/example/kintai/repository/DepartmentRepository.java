// src/main/java/com/example/kintai/repository/DepartmentRepository.java
package com.example.kintai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kintai.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    // これだけで findById() や findAll() 等が使えます
}