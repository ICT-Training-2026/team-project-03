package com.example.kintai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity  // JPAエンティティを示すアノテーション
@Table(name = "work")  // データベースのテーブル名を指定
@Getter  // Lombokによるゲッター自動生成
@Setter  // Lombokによるセッター自動生成
public class AttendanceType {

    @Id  // 主キーを示すアノテーション
    @Column(name = "ATT_ID")  // データベースのカラム名を指定
    private String attendanceTypeId;  // 出勤区分ID → String に変更

    @Column(name = "ATT_NAME", length = 50, nullable = false)  // 出勤区分名のカラム設定
    private String attendanceTypeName;  // 出勤区分名
}