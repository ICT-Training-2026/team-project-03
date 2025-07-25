package com.example.kintai.repository;

import java.sql.Date;
import java.util.List;

import com.example.kintai.entity.Kintai;

public interface KintaiRepository {
    // 勤怠登録
    void insert(Kintai k);

    // 個人別勤怠取得
    List<Kintai> selectByUserAndDate(String userId, Date date);

    // 期間指定の個人勤怠
    List<Kintai> selectByUserAndPeriod(String userId, Date startDate, Date endDate);

    // 部署別勤怠
    List<Kintai> selectByDepartmentAndPeriod(String departmentId, Date startDate, Date endDate);
}