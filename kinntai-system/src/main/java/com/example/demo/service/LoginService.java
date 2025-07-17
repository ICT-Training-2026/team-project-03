package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;

    public boolean authenticate(String userId, String rawPassword) {
        return userRepository.findById(userId)
                .map(user -> user.getPassword().equals(rawPassword)) // 実際はハッシュ比較
                .orElse(false);
    }
}