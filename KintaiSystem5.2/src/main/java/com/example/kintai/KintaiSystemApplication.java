package com.example.kintai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // Spring Bootアプリケーションのエントリーポイントを示すアノテーション
public class KintaiSystemApplication {

    // アプリケーションのメインメソッド
    public static void main(String[] args) {
        // Spring Bootアプリケーションを起動
        SpringApplication.run(KintaiSystemApplication.class, args);
    }
}