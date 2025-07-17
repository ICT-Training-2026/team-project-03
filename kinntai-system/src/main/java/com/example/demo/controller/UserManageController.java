package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.User;
import com.example.demo.service.UserManageService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserManageController {
    private final UserManageService userManageService;

    @PostMapping("/sinki/confirm")
    public String confirm(@ModelAttribute User user, Model model) {
        model.addAttribute("user", user);
        return "confirmShinki";
    }

    @PostMapping("/sinki/complete")
    public String complete(@ModelAttribute User user) {
        userManageService.save(user);
        return "completeShinki";
    }
}