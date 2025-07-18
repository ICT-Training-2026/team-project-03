package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.LoginService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String userId,
                          @RequestParam String password,
                          Model model) {
        if (loginService.authenticate(userId, password)) {
            // TODO: セッションにユーザ情報を入れるなど
            return "redirect:/main";
        } else {
            model.addAttribute("errorMsg", "IDまたはパスワードが間違っています");
            return "login";
        }
    }
}