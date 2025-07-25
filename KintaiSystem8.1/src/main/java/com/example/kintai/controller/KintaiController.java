package com.example.kintai.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.kintai.dto.KintaiRegisterDto;
import com.example.kintai.entity.Kintai;
import com.example.kintai.service.KintaiService;
import com.example.kintai.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/kintai")
@CrossOrigin
@RequiredArgsConstructor
public class KintaiController {

    private final KintaiService kintaiService;
    private final UserService userService;
    @PostMapping("/register")
    public String register(@RequestBody KintaiRegisterDto dto) {
        Kintai k = new Kintai();

        // 日付
        LocalDate ld = LocalDate.parse(dto.getDate());
        k.setDate(java.sql.Date.valueOf(ld));
        k.setYear(ld.getYear());
        k.setMonth(ld.getMonthValue());
        k.setDay(ld.getDayOfMonth());

        // 出勤時刻
        if (dto.getStartTime() != null && dto.getStartTime().contains(":")) {
            String[] s = dto.getStartTime().split(":");
            k.setInTimeH(s[0]);
            k.setInTimeM(s[1]);
        }
        // 退勤時刻
        if (dto.getEndTime() != null && dto.getEndTime().contains(":")) {
            String[] e = dto.getEndTime().split(":");
            k.setOutTimeH(e[0]);
            k.setOutTimeM(e[1]);
        }

        // ユーザーID
        k.setUserId(dto.getUserId());

        // ユーザー情報から部署IDを取得してセット
        userService.findByUserId(dto.getUserId()).ifPresent(user -> k.setDepartId(user.getDepartmentId()));

        // 勤怠区分ID
        k.setAttId(dto.getStatus());

        // DB登録
        kintaiService.registerKintai(k);
        return "OK";
    }

    // 個人別勤怠取得
    @GetMapping("/user")
    public List<Kintai> getUserKintai(
            @RequestParam String userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return kintaiService.getKintaiByUserAndPeriod(
                userId,
                LocalDate.parse(startDate),
                LocalDate.parse(endDate));
    }

    // 部署別勤怠取得
    @GetMapping("/department")
    public List<Kintai> getDepartmentKintai(
            @RequestParam String departmentId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return kintaiService.getKintaiByDepartmentAndPeriod(
                departmentId,
                LocalDate.parse(startDate),
                LocalDate.parse(endDate));
    }
}