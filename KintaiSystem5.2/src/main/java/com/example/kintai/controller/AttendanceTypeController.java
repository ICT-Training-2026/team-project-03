package com.example.kintai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kintai.entity.AttendanceType;
import com.example.kintai.service.AttendanceTypeService;

// RESTful APIのコントローラーを定義するクラス
@RestController
@RequestMapping("/api/attendanceType") // このコントローラーが処理するリクエストのベースURL
@CrossOrigin // CORSを許可するアノテーション
public class AttendanceTypeController {

    // AttendanceTypeServiceのインスタンスを自動的に注入
    @Autowired
    private AttendanceTypeService attendanceTypeService;

    // 全ての勤怠タイプを取得するためのGETリクエストを処理するメソッド
    @GetMapping("/all") // "/all"エンドポイントへのGETリクエストを処理
    public List<AttendanceType> getAllAttendanceTypes() {
        // AttendanceTypeServiceを使用して全ての勤怠タイプを取得し、返す
        return attendanceTypeService.getAllAttendanceTypes();
    }
}