package com.example.kintai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example.kintai") // 明示的にスキャン対象を指定
public class KintaiSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(KintaiSystemApplication.class, args);
    }
}