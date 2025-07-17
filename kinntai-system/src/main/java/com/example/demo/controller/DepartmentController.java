package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.DepartmentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping("/department")
    public String showDepartment(@RequestParam(required = false) String departmentId, Model model) {
        if (departmentId != null && !departmentId.isEmpty()) {
            model.addAttribute("kintaiList", departmentService.getKintaiByDepartment(departmentId));
        }
        return "department";
    }

    @GetMapping("/department/csv")
    public String downloadCsv() {
        // CSVダウンロード処理（後で実装）
        return "redirect:/department";
    }
}