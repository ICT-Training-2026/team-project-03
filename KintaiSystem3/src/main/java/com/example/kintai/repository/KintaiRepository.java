package com.example.kintai.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kintai.entity.Kintai;

@Repository
public interface KintaiRepository extends JpaRepository<Kintai, Long> {

    // ユーザーIDと期間で勤怠情報を検索
    List<Kintai> findByUser_UserIdAndDateBetween(String userId, LocalDate start, LocalDate end);

    // 部署IDと期間で勤怠情報を検索
    List<Kintai> findByDepartment_DepartmentIdAndDateBetween(String departmentId, LocalDate start, LocalDate end);
}