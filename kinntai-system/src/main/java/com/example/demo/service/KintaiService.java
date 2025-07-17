package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Kintai;
import com.example.demo.repository.KintaiRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KintaiService {

    private final KintaiRepository repository;

    public void regist(Kintai kintai) {
        repository.save(kintai);
    }
}
