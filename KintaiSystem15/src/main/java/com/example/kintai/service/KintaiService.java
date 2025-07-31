// 勤怠に関するビジネスロジックを処理するサービス層クラスです。
// @Service アノテーションにより、Springによって管理されるコンポーネントになります。
// @RequiredArgsConstructor は Lombok のアノテーションで、finalなフィールドを自動でDI（依存性注入）します。
package com.example.kintai.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

// データベース操作を行うリポジトリクラス
import com.example.kintai.entity.Kintai;
import com.example.kintai.repository.KintaiRepository;

import lombok.RequiredArgsConstructor;

/**
 * 勤怠管理のビジネスロジックを提供するサービスです。
 * コントローラとリポジトリの間でデータの処理と変換を行います。
 */
@Service
@RequiredArgsConstructor
public class KintaiService {

    private final KintaiRepository kintaiRepository;

    // --- 勤怠区分IDの内部マッピング（Axxx形式から1桁数字へ変換）---
    private static final Map<String, String> ATT_ID_TO_SINGLE_DIGIT_MAP = new HashMap<>();
    static {
        ATT_ID_TO_SINGLE_DIGIT_MAP.put("A001", "1");
        ATT_ID_TO_SINGLE_DIGIT_MAP.put("A002", "2");
        ATT_ID_TO_SINGLE_DIGIT_MAP.put("A003", "3");
        ATT_ID_TO_SINGLE_DIGIT_MAP.put("A004", "4");
        ATT_ID_TO_SINGLE_DIGIT_MAP.put("A005", "5");
        ATT_ID_TO_SINGLE_DIGIT_MAP.put("A006", "6");
    }

    // --- 勤怠区分IDの内部逆マッピング（1桁数字からAxxx形式へ変換）---
    private static final Map<String, String> SINGLE_DIGIT_TO_ATT_ID_MAP = new HashMap<>();
    static {
        SINGLE_DIGIT_TO_ATT_ID_MAP.put("1", "A001");
        SINGLE_DIGIT_TO_ATT_ID_MAP.put("2", "A002");
        SINGLE_DIGIT_TO_ATT_ID_MAP.put("3", "A003");
        SINGLE_DIGIT_TO_ATT_ID_MAP.put("4", "A004");
        SINGLE_DIGIT_TO_ATT_ID_MAP.put("5", "A005");
        SINGLE_DIGIT_TO_ATT_ID_MAP.put("6", "A006");
    }

    /**
     * 新しい勤怠情報を登録します。
     * @param kintaiRecord 登録する勤怠情報
     */
    public void registerKintai(Kintai kintaiRecord) {
        // 勤怠区分IDを1桁の数字文字列に変換して設定 (DB保存用)
        String convertedAttId = ATT_ID_TO_SINGLE_DIGIT_MAP.getOrDefault(kintaiRecord.getAttId(), kintaiRecord.getAttId());
        kintaiRecord.setAttId(convertedAttId);
        kintaiRepository.insert(kintaiRecord);
    }

    /**
     * 既存の勤怠情報を更新します。
     * @param kintaiRecord 更新する勤怠情報
     */
    public void updateKintai(Kintai kintaiRecord) {
        // 勤怠区分IDを1桁の数字文字列に変換して設定 (DB保存用)
        String convertedAttId = ATT_ID_TO_SINGLE_DIGIT_MAP.getOrDefault(kintaiRecord.getAttId(), kintaiRecord.getAttId());
        kintaiRecord.setAttId(convertedAttId);
        kintaiRepository.update(kintaiRecord);
    }

    /**
     * 指定されたユーザーと日付の勤怠情報がデータベースに存在するかどうかを確認します。
     * @param userId ユーザーID
     * @param date 確認したい日付（LocalDateオブジェクト）
     * @return 勤怠情報が存在すれば true、存在しなければ false を返します。
     */
    public boolean existsKintai(String userId, LocalDate date) {
        Date sqlDate = Date.valueOf(date);
        List<Kintai> existingKintai = kintaiRepository.selectByUserAndDate(userId, sqlDate);
        return !existingKintai.isEmpty();
    }

    /**
     * 特定のユーザーの指定された期間の勤怠情報を取得します。
     * @param userId ユーザーID
     * @param startDate 検索期間の開始日（LocalDateオブジェクト）
     * @param endDate 検索期間の終了日（LocalDateオブジェクト）
     * @return 該当する勤怠情報のリスト（Kintaiオブジェクトのリスト）
     */
    public List<Kintai> getUserKintaiByPeriod(String userId, LocalDate startDate, LocalDate endDate) {
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);
        List<Kintai> kintaiList = kintaiRepository.selectByUserAndPeriod(userId, sqlStartDate, sqlEndDate);

        // DBから取得したATT_ID（1桁数字）をフロントエンドの表示用ID（Axxx形式）に逆変換
        kintaiList.forEach(kintaiRecord -> {
            String convertedAttId = SINGLE_DIGIT_TO_ATT_ID_MAP.getOrDefault(kintaiRecord.getAttId(), kintaiRecord.getAttId());
            kintaiRecord.setAttId(convertedAttId);
        });

        return kintaiList;
    }

    /**
     * 特定の部署の指定された期間の勤怠情報を取得します。
     * @param departmentId 部署ID
     * @param startDate 検索期間の開始日（LocalDateオブジェクト）
     * @param endDate 検索期間の終了日（LocalDateオブジェクト）
     * @return 該当する勤怠情報（Kintaiオブジェクト）のリスト。
     */
    public List<Kintai> getDepartmentKintaiByPeriod(String departmentId, LocalDate startDate, LocalDate endDate) {
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);
        // DBから取得したATT_ID（1桁数字）をフロントエンドの表示用ID（Axxx形式）に逆変換
        List<Kintai> kintaiList = kintaiRepository.selectByDepartmentAndPeriod(departmentId, sqlStartDate, sqlEndDate);
        kintaiList.forEach(kintaiRecord -> {
            String convertedAttId = SINGLE_DIGIT_TO_ATT_ID_MAP.getOrDefault(kintaiRecord.getAttId(), kintaiRecord.getAttId());
            kintaiRecord.setAttId(convertedAttId);
        });
        return kintaiList;
    }
}