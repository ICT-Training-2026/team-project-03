package com.example.kintai.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kintai.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    // ログイン認証用
    Optional<User> findByUserIdAndPass(String userId, String pass);

    // 部署IDで一覧取得なども必要なら
    // List<User> findByDepartment_DepartmentId(String departmentId);
}