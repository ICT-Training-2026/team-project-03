package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Kintai;
import com.example.demo.service.KintaiService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class KintaiController {

    private final KintaiService service;

    // 打刻画面の表示
    @GetMapping("/kintai")
    public String showKintaiForm(Model model) {
        model.addAttribute("kintai", new Kintai());
        return "kintai";
    }

    // 確認画面へ
    @PostMapping("/confirmKintai")
    public String confirmKintai(@ModelAttribute Kintai kintai, Model model) {
        model.addAttribute("kintai", kintai);
        return "confirmKintai";
    }

    // 完了画面へ
    @PostMapping("/completeKintai")
    public String completeKintai(@ModelAttribute Kintai kintai) {
        service.regist(kintai);
        return "completeKintai";
    }
}