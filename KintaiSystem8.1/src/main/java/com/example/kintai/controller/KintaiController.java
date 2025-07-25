// 勤怠情報に関するAPIをまとめたコントローラークラスです。
// @RestController を使うと、各メソッドの戻り値が直接HTTPレスポンスのボディになります。
// @RequestMapping("/api/kintai") で、このコントローラー内のすべてのAPIが "/api/kintai" から始まるURLになります。
// @CrossOrigin は、異なるドメインからのリクエストも許可するための設定です（開発時に便利）。
// @RequiredArgsConstructor は Lombok のアノテーションで、finalなフィールドを自動でDI（依存性注入）します。
package com.example.kintai.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// DTO (Data Transfer Object): フロントエンドとバックエンド間でデータをやり取りするためのクラス
import com.example.kintai.dto.KintaiRegisterDto;
// Entity (データベースのテーブルに対応するJavaオブジェクト)
import com.example.kintai.entity.Kintai;
// Service (ビジネスロジックを扱うクラス)
import com.example.kintai.service.KintaiService;
import com.example.kintai.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/kintai")
@CrossOrigin
@RequiredArgsConstructor
public class KintaiController {

    // KintaiService と UserService をSpringが自動的に注入します（DI）。
    // これらを使って勤怠やユーザーに関する処理を行います。
    private final KintaiService kintaiService;
    private final UserService userService;

    /**
     * 勤怠情報を登録または更新するAPI。
     * HTTP POSTリクエストを "/api/kintai/register" で受け取ります。
     * @param kintaiDto フロントエンドから送信された勤怠登録データ（KintaiRegisterDtoオブジェクト）
     * @return 処理が成功したら "OK" という文字列を返します。
     */
    @PostMapping("/register")
    public String registerOrUpdateKintai(@RequestBody KintaiRegisterDto kintaiDto) {
        // DTOから受け取ったデータを使って、データベースに保存するKintaiオブジェクトを作成します。
        Kintai kintaiRecord = new Kintai();

        // --- 日付情報の変換と設定 ---
        // 文字列形式の日付 (yyyy-MM-dd) をLocalDateオブジェクトに変換
        LocalDate localDate = LocalDate.parse(kintaiDto.getDate());
        // JavaのDate型に変換してKintaiオブジェクトに設定
        kintaiRecord.setDate(java.sql.Date.valueOf(localDate));
        // 年、月、日をそれぞれ数値としてKintaiオブジェクトに設定
        kintaiRecord.setYear(localDate.getYear());
        kintaiRecord.setMonth(localDate.getMonthValue()); // 月は1から12の値
        kintaiRecord.setDay(localDate.getDayOfMonth());

        // --- 出勤時刻の設定 ---
        // start_timeが空でなく、かつ":"を含む場合にのみ処理
        if (kintaiDto.getStartTime() != null && kintaiDto.getStartTime().contains(":")) {
            // "HH:mm"形式の文字列を":"で分割して、時と分をそれぞれ設定
            String[] startTimeParts = kintaiDto.getStartTime().split(":");
            kintaiRecord.setInTimeH(startTimeParts[0]);
            kintaiRecord.setInTimeM(startTimeParts[1]);
        }
        // --- 退勤時刻の設定 ---
        // end_timeが空でなく、かつ":"を含む場合にのみ処理
        if (kintaiDto.getEndTime() != null && kintaiDto.getEndTime().contains(":")) {
            // "HH:mm"形式の文字列を":"で分割して、時と分をそれぞれ設定
            String[] endTimeParts = kintaiDto.getEndTime().split(":");
            kintaiRecord.setOutTimeH(endTimeParts[0]);
            kintaiRecord.setOutTimeM(endTimeParts[1]);
        }

        // --- ユーザーIDの設定 ---
        kintaiRecord.setUserId(kintaiDto.getUserId());

        // --- ユーザーIDから部署IDを取得して設定 ---
        // UserServiceを使って、ユーザーIDからユーザー情報を取得します。
        // userが見つかった場合（isPresent）、そのユーザーの部署IDを勤怠レコードに設定します。
        userService.findByUserId(kintaiDto.getUserId())
                   .ifPresent(user -> kintaiRecord.setDepartId(user.getDepartmentId()));

        // --- 勤怠区分IDの設定 ---
        kintaiRecord.setAttId(kintaiDto.getStatus()); // String型として扱うように戻す

        // --- データベースへの登録または更新処理 ---
        // 同じユーザーIDと日付の勤怠情報が既に存在するかをチェックします。
        if (kintaiService.existsKintai(kintaiRecord.getUserId(), localDate)) {
            // 既存の勤怠情報がある場合、更新処理を実行
            kintaiService.updateKintai(kintaiRecord);
        } else {
            // 既存の勤怠情報がない場合、新規登録処理を実行
            kintaiService.registerKintai(kintaiRecord);
        }

        return "OK"; // 処理成功をフロントエンドに伝える
    }

    /**
     * 特定のユーザーと期間の勤怠情報を取得するAPI。
     * HTTP GETリクエストを "/api/kintai/user" で受け取ります。
     * @param userId 取得したいユーザーのID
     * @param startDate 検索期間の開始日 (yyyy-MM-dd)
     * @param endDate 検索期間の終了日 (yyyy-MM-dd)
     * @return 該当する勤怠情報のリスト（Kintaiオブジェクトのリスト）
     */
    @GetMapping("/user")
    public List<Kintai> getUserKintaiByPeriod(
            @RequestParam String userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        // サービス層を呼び出して勤怠情報を取得し、そのまま返します。
        return kintaiService.getUserKintaiByPeriod( // メソッド名を修正
                userId,
                LocalDate.parse(startDate),
                LocalDate.parse(endDate));
    }

    /**
     * 特定の部署と期間の勤怠情報を取得するAPI。
     * HTTP GETリクエストを "/api/kintai/department" で受け取ります。
     * @param departmentId 取得したい部署のID
     * @param startDate 検索期間の開始日 (yyyy-MM-dd)
     * @param endDate 検索期間の終了日 (yyyy-MM-dd)
     * @return 該当する勤怠情報のリスト（Kintaiオブジェクトのリスト）
     */
    @GetMapping("/department")
    public List<Kintai> getDepartmentKintaiByPeriod(
            @RequestParam String departmentId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        // サービス層を呼び出して勤怠情報を取得し、そのまま返します。
        return kintaiService.getDepartmentKintaiByPeriod( // メソッド名を修正
                departmentId,
                LocalDate.parse(startDate),
                LocalDate.parse(endDate));
    }
}