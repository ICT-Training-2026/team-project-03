package com.example.kintai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.kintai.entity.AttendanceType;
import com.example.kintai.repository.AttendanceTypeRepository;

@Service
public class AttendanceTypeService {

    @Autowired
    private AttendanceTypeRepository attendanceTypeRepository;  // AttendanceTypeRepositoryのインスタンスを自動的に注入

    /**
     * 全ての勤怠区分を取得するメソッド
     * 
     * @return 勤怠区分のリスト
     */
    public List<AttendanceType> getAllAttendanceTypes() {
        return attendanceTypeRepository.findAll();  // リポジトリを使用して全ての勤怠区分を取得
    }
}