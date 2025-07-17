package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // ログイン用: IDとパスワードで検索
    User findByUserIdAndPass(String userId, String pass);

    // 部署IDでユーザー一覧を取得
    java.util.List<User> findByDepartmentId(String departmentId);
}