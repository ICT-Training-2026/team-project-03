package com.example.kintai.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.kintai.entity.Kintai;
import com.example.kintai.repository.KintaiRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KintaiService {

    private final KintaiRepository kintaiRepository;

    public void registerKintai(Kintai k) {
        kintaiRepository.insert(k);
    }

    public List<Kintai> getKintaiByUserAndPeriod(String userId, LocalDate start, LocalDate end) {
        return kintaiRepository.selectByUserAndPeriod(userId, Date.valueOf(start), Date.valueOf(end));
    }

    public List<Kintai> getKintaiByDepartmentAndPeriod(String deptId, LocalDate start, LocalDate end) {
        return kintaiRepository.selectByDepartmentAndPeriod(deptId, Date.valueOf(start), Date.valueOf(end));
    }
}