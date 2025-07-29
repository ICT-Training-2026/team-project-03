// 勤怠に関するビジネスロジックを処理するサービス層クラスです。
// @Service アノテーションにより、Springによって管理されるコンポーネントになります。
// @RequiredArgsConstructor は Lombok のアノテーションで、finalなフィールドを自動でDI（依存性注入）します。
package com.example.kintai.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

// データベース操作を行うリポジトリクラス
import com.example.kintai.entity.Kintai;
import com.example.kintai.repository.KintaiRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KintaiService {

    // KintaiRepository をSpringが自動的に注入します（DI）。
    // これを使ってデータベースへのアクセスを行います。
    private final KintaiRepository kintaiRepository;

    /**
     * 新しい勤怠情報をデータベースに登録します。
     * @param kintaiRecord 登録する勤怠情報（Kintaiオブジェクト）
     */
    public void registerKintai(Kintai kintaiRecord) {
        // KintaiRepository の insert メソッドを呼び出して、データベースに保存します。
        kintaiRepository.insert(kintaiRecord);
    }

    /**
     * 特定のユーザーの特定の日付の勤怠情報がデータベースに存在するかどうかを確認します。
     * @param userId ユーザーID
     * @param date 確認したい日付（LocalDateオブジェクト）
     * @return 勤怠情報が存在すれば true、存在しなければ false を返します。
     */
    public boolean existsKintai(String userId, LocalDate date) {
        // KintaiRepository の selectByUserAndDate メソッドを呼び出し、結果が空でなければ存在すると判断します。
        return !kintaiRepository.selectByUserAndDate(userId, java.sql.Date.valueOf(date)).isEmpty();
    }

    /**
     * 既存の勤怠情報をデータベースで更新します。
     * @param kintaiRecord 更新する勤怠情報（Kintaiオブジェクト）
     */
    public void updateKintai(Kintai kintaiRecord) {
        // KintaiRepository の update メソッドを呼び出して、データベースの情報を更新します。
        kintaiRepository.update(kintaiRecord);
    }

    /**
     * 特定のユーザーの指定された期間の勤怠情報を取得します。
     * @param userId ユーザーID
     * @param startDate 検索期間の開始日（LocalDateオブジェクト）
     * @param endDate 検索期間の終了日（LocalDateオブジェクト）
     * @return 該当する勤怠情報のリスト（Kintaiオブジェクトのリスト）
     */
    public List<Kintai> getKintaiByUserAndPeriod(String userId, LocalDate startDate, LocalDate endDate) {
        // KintaiRepository の selectByUserAndPeriod メソッドを呼び出して勤怠情報を取得します。
        return kintaiRepository.selectByUserAndPeriod(userId, java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate));
    }

    /**
     * 特定の部署の指定された期間の勤怠情報を取得します。
     * @param departmentId 部署ID
     * @param startDate 検索期間の開始日（LocalDateオブジェクト）
     * @param endDate 検索期間の終了日（LocalDateオブジェクト）
     * @return 該当する勤怠情報のリスト（Kintaiオブジェクトのリスト）
     */
    public List<Kintai> getKintaiByDepartmentAndPeriod(String departmentId, LocalDate startDate, LocalDate endDate) {
        // KintaiRepository の selectByDepartmentAndPeriod メソッドを呼び出して勤怠情報を取得します。
        return kintaiRepository.selectByDepartmentAndPeriod(departmentId, java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate));
    }
}