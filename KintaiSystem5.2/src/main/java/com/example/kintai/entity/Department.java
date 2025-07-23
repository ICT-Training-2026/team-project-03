// src/main/java/com/example/kintai/entity/Department.java
package com.example.kintai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity  // JPAエンティティを示すアノテーション
@Table(name = "department")  // データベースのテーブル名を指定
public class Department {

    @Id  // 主キーを示すアノテーション
    @Column(name = "depart_id")  // データベースのカラム名を指定
    private String departId;  // 部署ID

    @Column(name = "depart_name")  // データベースのカラム名を指定
    private String departName;  // 部署名

    // --- getters/setters ---
    public String getDepartId() { 
        return departId;  // 部署IDを取得するゲッター
    }

    public void setDepartId(String departId) { 
        this.departId = departId;  // 部署IDを設定するセッター
    }

    public String getDepartName() { 
        return departName;  // 部署名を取得するゲッター
    }

    public void setDepartName(String departName) { 
        this.departName = departName;  // 部署名を設定するセッター
    }
}