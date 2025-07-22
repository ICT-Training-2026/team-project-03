package com.example.kintai.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.kintai.entity.Kintai;
import com.example.kintai.repository.KintaiRepository;

@Service
public class KintaiService {

    @Autowired
    private KintaiRepository kintaiRepository;

    // 勤怠登録
    public Kintai registerKintai(Kintai kintai) {
        return kintaiRepository.save(kintai);
    }

    // ユーザーIDと期間で勤怠取得
    public List<Kintai> getKintaiByUserAndPeriod(String userId, LocalDate start, LocalDate end) {
        return kintaiRepository.findByUser_UserIdAndDateBetween(userId, start, end);
    }

    // 部署IDと期間で勤怠取得
    public List<Kintai> getKintaiByDepartmentAndPeriod(Integer departmentId, LocalDate start, LocalDate end) {
        return kintaiRepository.findByDepartment_DepartmentIdAndDateBetween(departmentId, start, end);
    }
}