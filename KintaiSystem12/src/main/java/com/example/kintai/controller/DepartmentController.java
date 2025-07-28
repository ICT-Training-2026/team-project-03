package com.example.kintai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kintai.entity.Department;
import com.example.kintai.service.DepartmentService;
//package, import は省略

@RestController
@RequestMapping("/api/department")
@CrossOrigin
public class DepartmentController {

 @Autowired
 private DepartmentService departmentService;

 @GetMapping("/all")
 public List<Department> getAllDepartments() {
     List<Department> list = departmentService.getAllDepartments();
     // D004 → D003 に書き換え
     list.forEach(d -> {
         if ("D004".equals(d.getDepartId())) {
             d.setDepartId("D003");
         }
     });
     return list;
 }
}