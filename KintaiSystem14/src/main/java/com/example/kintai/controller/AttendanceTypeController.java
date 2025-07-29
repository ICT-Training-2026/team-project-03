package com.example.kintai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kintai.entity.AttendanceType;
import com.example.kintai.service.AttendanceTypeService;

@RestController
@RequestMapping("/api/attendanceType")
@CrossOrigin
public class AttendanceTypeController {

    @Autowired
    private AttendanceTypeService attendanceTypeService;

    @GetMapping("/all")
    public List<AttendanceType> getAllAttendanceTypes() {
        return attendanceTypeService.getAllAttendanceTypes();
    }
}