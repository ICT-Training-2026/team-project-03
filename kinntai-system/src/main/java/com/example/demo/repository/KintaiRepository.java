package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import com.example.demo.entity.Kintai;

@Repository
public class KintaiRepository {
    // TODO: DB実装後、JPAやMyBatisで処理
    public void save(Kintai kintai) {
        // 仮実装：本来はINSERT処理
        System.out.println("勤怠登録：" + kintai.toString());
    }
}