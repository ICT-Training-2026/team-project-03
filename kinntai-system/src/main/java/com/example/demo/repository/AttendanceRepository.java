package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.AttInfo;

@Repository
public interface AttInfoRepository extends JpaRepository<AttInfo, String> {
    // 必要なら追加
}