package com.example.kintai.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.kintai.entity.User;
import com.example.kintai.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 新規ユーザー登録：常に BCrypt でハッシュ化して保存
     */
    public User saveUser(User user, String rawPassword) {
        String hashed = passwordEncoder.encode(rawPassword);
        user.setPass(hashed);
        return userRepository.save(user);
    }

    /**
     * ログイン認証
     * 1) DB のパスワードが BCrypt ハッシュ形式なら passwordEncoder.matches() で照合
     * 2) そうでなければ「legacy 平文」と判断し、equals() で照合。
     *    成功時には即座にハッシュ化して上書き保存する
     */
    public Optional<User> login(String userId, String rawPassword) {
        return userRepository.findByUserId(userId)
            .flatMap(u -> {
                String stored = u.getPass();
                if (stored != null && stored.startsWith("$2a$")) {
                    // すでにハッシュ化済み
                    return passwordEncoder.matches(rawPassword, stored)
                         ? Optional.of(u)
                         : Optional.empty();
                } else {
                    // legacy：平文保存中
                    if (stored != null && stored.equals(rawPassword)) {
                        // 平文認証成功 → 即ハッシュ化して保存
                        u.setPass(passwordEncoder.encode(rawPassword));
                        userRepository.save(u);
                        return Optional.of(u);
                    } else {
                        return Optional.empty();
                    }
                }
            });
    }

    /** userId でユーザー取得 */
    public Optional<User> findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    /** 全ユーザー取得（管理者用） */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}