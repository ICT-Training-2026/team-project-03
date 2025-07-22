package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Kintai;
import com.example.demo.repository.KintaiRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final KintaiRepository kintaiRepository;

    public List<Kintai> getKintaiByDepartment(String departmentId) {
        return kintaiRepository.findByDepartId(departmentId);
    }

    // CSV出力用メソッドも追加可能
}