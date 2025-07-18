package com.example.kintai.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.kintai.entity.Kintai;
import com.example.kintai.service.KintaiService;

@RestController
@RequestMapping("/api/kintai")
@CrossOrigin
public class KintaiController {

    @Autowired
    private KintaiService kintaiService;

    // 勤怠登録
    @PostMapping("/register")
    public void register(@RequestBody Kintai kintai) {
        kintaiService.registerKintai(kintai);
    }

    // 個人別勤怠取得
    @GetMapping("/user")
    public List<Kintai> getUserKintai(
            @RequestParam String userId,          // ★ String に変更
            @RequestParam String startDate,
            @RequestParam String endDate) {

        return kintaiService.getKintaiByUserAndPeriod(
            userId,
            LocalDate.parse(startDate),
            LocalDate.parse(endDate)
        );
    }

    // 部署別勤怠取得
    @GetMapping("/department")
    public List<Kintai> getDepartmentKintai(
            @RequestParam Integer departmentId,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        return kintaiService.getKintaiByDepartmentAndPeriod(
            departmentId,
            LocalDate.parse(startDate),
            LocalDate.parse(endDate)
        );
    }
}