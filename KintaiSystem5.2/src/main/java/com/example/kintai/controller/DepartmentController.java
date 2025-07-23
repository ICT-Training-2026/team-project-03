package com.example.kintai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kintai.entity.Department;
import com.example.kintai.service.DepartmentService;

// RESTful APIのコントローラーを定義するクラス
@RestController
@RequestMapping("/api/department") // このコントローラーが処理するリクエストのベースURL
@CrossOrigin // CORSを許可するアノテーション
public class DepartmentController {

    // DepartmentServiceのインスタンスを自動的に注入
    @Autowired
    private DepartmentService departmentService;

    // 全ての部署を取得するためのGETリクエストを処理するメソッド
    @GetMapping("/all") // "/all"エンドポイントへのGETリクエストを処理
    public List<Department> getAllDepartments() {
        // DepartmentServiceを使用して全ての部署を取得し、返す
        return departmentService.getAllDepartments();
    }
}