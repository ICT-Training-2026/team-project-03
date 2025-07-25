package com.example.kintai.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.kintai.entity.User;
import com.example.kintai.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 全ユーザー取得
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ユーザー登録（1件）
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // ログイン認証
    public Optional<User> login(String userId, String rawPass) {
        // パスワードは実際はハッシュ比較が必要
        return userRepository.findByUserIdAndPass(userId, rawPass);
    }

    // userIdでユーザー取得
    public Optional<User> findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }
}