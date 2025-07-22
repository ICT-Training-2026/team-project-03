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

import com.example.kintai.dto.KintaiRegisterDto;
import com.example.kintai.entity.AttendanceType;
import com.example.kintai.entity.Kintai;
import com.example.kintai.entity.User;
import com.example.kintai.repository.AttendanceTypeRepository;
import com.example.kintai.repository.UserRepository;
import com.example.kintai.service.KintaiService;

@RestController
@RequestMapping("/api/kintai")
@CrossOrigin
public class KintaiController {

    @Autowired
    private KintaiService kintaiService;

    // ✅ 追加: Repositoryを直接参照するためのフィールド
    @Autowired
    private AttendanceTypeRepository attendanceTypeRepository;

    @Autowired
    private UserRepository userRepository;

    // -----------------------------
    // 勤怠登録（修正後）
    // -----------------------------
    @PostMapping("/register")
    public void register(@RequestBody KintaiRegisterDto dto) {
        Kintai k = new Kintai();

        // 日付
        LocalDate ld = LocalDate.parse(dto.getDate()); // "yyyy-MM-dd"
        k.setDate(java.sql.Date.valueOf(ld));
        k.setYear(ld.getYear());
        k.setMonth(ld.getMonthValue());
        k.setDay(ld.getDayOfMonth());

        // 時刻
        if (dto.getStartTime() != null && dto.getStartTime().contains(":")) {
            String[] parts = dto.getStartTime().split(":");
            k.setInTimeH(parts[0]);
            k.setInTimeM(parts[1]);
        }
        if (dto.getEndTime() != null && dto.getEndTime().contains(":")) {
            String[] parts = dto.getEndTime().split(":");
            k.setOutTimeH(parts[0]);
            k.setOutTimeM(parts[1]);
        }

        // 勤怠区分
        AttendanceType att = attendanceTypeRepository.findById(dto.getStatus()).orElse(null);
        k.setAttendanceType(att);

        // ユーザー
        User u = userRepository.findById(dto.getUserId()).orElse(null);
        k.setUser(u);

        // 部署（Userから取得）
        if (u != null) {
            k.setDepartment(u.getDepartment());
        }

        // 保存
        kintaiService.registerKintai(k);
    }

    // -----------------------------
    // 個人別勤怠取得
    // -----------------------------
    @GetMapping("/user")
    public List<Kintai> getUserKintai(
            @RequestParam String userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        return kintaiService.getKintaiByUserAndPeriod(
            userId,
            LocalDate.parse(startDate),
            LocalDate.parse(endDate)
        );
    }

    // -----------------------------
    // 部署別勤怠取得
    // -----------------------------
    @GetMapping("/department")
    public List<Kintai> getDepartmentKintai(
            @RequestParam String departmentId,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        return kintaiService.getKintaiByDepartmentAndPeriod(
            departmentId,
            LocalDate.parse(startDate),
            LocalDate.parse(endDate)
        );
    }
}