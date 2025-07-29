package com.example.kintai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginPageController {

    // 「/login」にアクセスしたときに login.html を返す
    @GetMapping("/login")
    public String showLoginPage() {
        // templates フォルダ内の login.html を返す
        return "login";
    }
}