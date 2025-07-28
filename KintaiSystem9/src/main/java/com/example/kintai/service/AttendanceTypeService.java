package com.example.kintai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.kintai.entity.AttendanceType;
import com.example.kintai.repository.AttendanceTypeRepository;

@Service
public class AttendanceTypeService {

    @Autowired
    private AttendanceTypeRepository attendanceTypeRepository;

    public List<AttendanceType> getAllAttendanceTypes() {
        return attendanceTypeRepository.findAll();
    }
}