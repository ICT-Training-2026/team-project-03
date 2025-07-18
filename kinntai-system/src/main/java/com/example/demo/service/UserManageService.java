package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserManageService {
    private final UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }
}