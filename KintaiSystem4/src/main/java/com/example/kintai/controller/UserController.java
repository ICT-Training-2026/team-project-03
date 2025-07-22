package com.example.kintai.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.kintai.entity.User;
import com.example.kintai.service.UserService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    // 全ユーザー取得（管理者用）
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // 新規登録（複数登録も受けられるように List で）
    @PostMapping("/register")
    public void registerUsers(@RequestBody List<User> users) {
        for (User u : users) {
            userService.saveUser(u);
        }
    }

    // ログイン認証（★ 元の状態に戻したメソッド）
    @PostMapping("/login")
    public User login(@RequestParam String userId, @RequestParam String pass) {
        Optional<User> user = userService.login(userId, pass);
        return user.orElse(null);
    }
}