package com.example.kintai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kintai.entity.Authority;
import com.example.kintai.service.AuthorityService;

// RESTful APIのコントローラーを定義するクラス
@RestController
@RequestMapping("/api/authority") // このコントローラーが処理するリクエストのベースURL
@CrossOrigin // CORSを許可するアノテーション
public class AuthorityController {

    // AuthorityServiceのインスタンスを自動的に注入
    @Autowired
    private AuthorityService authorityService;

    // 全ての権限を取得するためのGETリクエストを処理するメソッド
    @GetMapping("/all") // "/all"エンドポイントへのGETリクエストを処理
    public List<Authority> getAllAuthorities() {
        // AuthorityServiceを使用して全ての権限を取得し、返す
        return authorityService.getAllAuthorities();
    }
}