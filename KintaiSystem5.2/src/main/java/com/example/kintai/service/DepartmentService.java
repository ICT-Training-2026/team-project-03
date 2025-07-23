package com.example.kintai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.kintai.entity.Department;
import com.example.kintai.repository.DepartmentRepository;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;  // DepartmentRepositoryのインスタンスを自動的に注入

    /** 
     * 全ての部署を取得するメソッド
     * 
     * @return 部署のリスト
     */
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();  // リポジトリを使用して全ての部署を取得
    }
}