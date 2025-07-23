package com.example.kintai.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.kintai.entity.Kintai;
import com.example.kintai.repository.KintaiRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor  // コンストラクタインジェクションを使用するためのLombokアノテーション
public class KintaiService {

    private final KintaiRepository kintaiRepository;  // KintaiRepositoryのインスタンスを自動的に注入

    /** 
     * 勤怠情報を登録するメソッド
     * @param k 登録する勤怠情報のKintaiオブジェクト
     */
    public void registerKintai(Kintai k) {
        kintaiRepository.insert(k);  // KintaiRepositoryを使用して勤怠情報をデータベースに挿入
    }

    /** 
     * 特定のユーザーの勤怠情報を期間で取得するメソッド
     * @param userId ユーザーID
     * @param start  開始日
     * @param end    終了日
     * @return 指定されたユーザーの勤怠情報のリスト
     */
    public List<Kintai> getKintaiByUserAndPeriod(String userId, LocalDate start, LocalDate end) {
        return kintaiRepository.selectByUserAndPeriod(userId, Date.valueOf(start), Date.valueOf(end));  // ユーザーIDと期間に基づいて勤怠情報を取得
    }

    /** 
     * 特定の部署の勤怠情報を期間で取得するメソッド
     * @param deptId 部署ID
     * @param start  開始日
     * @param end    終了日
     * @return 指定された部署の勤怠情報のリスト
     */
    public List<Kintai> getKintaiByDepartmentAndPeriod(String deptId, LocalDate start, LocalDate end) {
        return kintaiRepository.selectByDepartmentAndPeriod(deptId, Date.valueOf(start), Date.valueOf(end));  // 部署IDと期間に基づいて勤怠情報を取得
    }
}