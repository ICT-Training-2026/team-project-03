package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Kintai;

@Repository
public interface KintaiRepository extends JpaRepository<Kintai, Long> {

    // ユーザごとの勤怠一覧
    List<Kintai> findByUserId(String userId);

    // 部署ごとの勤怠一覧
    List<Kintai> findByDepartId(String departId);

    // 特定日付や月ごとの検索が必要なら
    List<Kintai> findByDate(String date);
}