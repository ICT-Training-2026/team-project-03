package com.example.kintai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(
  exclude = SecurityAutoConfiguration.class  // Spring Security の自動設定を外す
)
public class KintaiSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(KintaiSystemApplication.class, args);
    }
}
