package com.example.kintai.entity;

import java.sql.Date;

public class Kintai {

    // ===== 既存フィールド =====
    private Date date;
    private int year;
    private int month;
    private int day;

    private String inTimeH;
    private String inTimeM;
    private String outTimeH;
    private String outTimeM;

    // ===== JDBC 直接用のフィールドを追加 =====
    private String userId;      // ユーザーID
    private String departId;    // 部署ID
    private String attId;       // 勤怠区分ID

    // ====== Getter / Setter ======

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getInTimeH() {
        return inTimeH;
    }

    public void setInTimeH(String inTimeH) {
        this.inTimeH = inTimeH;
    }

    public String getInTimeM() {
        return inTimeM;
    }

    public void setInTimeM(String inTimeM) {
        this.inTimeM = inTimeM;
    }

    public String getOutTimeH() {
        return outTimeH;
    }

    public void setOutTimeH(String outTimeH) {
        this.outTimeH = outTimeH;
    }

    public String getOutTimeM() {
        return outTimeM;
    }

    public void setOutTimeM(String outTimeM) {
        this.outTimeM = outTimeM;
    }

    // ====== 新しく追加したフィールドの Getter/Setter ======

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    public String getAttId() {
        return attId;
    }

    public void setAttId(String attId) {
        this.attId = attId;
    }
}