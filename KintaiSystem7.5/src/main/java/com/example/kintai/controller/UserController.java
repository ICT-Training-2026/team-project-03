// src/main/java/com/example/kintai/controller/UserController.java
package com.example.kintai.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.kintai.dto.UserRegisterDto;
import com.example.kintai.entity.User;
import com.example.kintai.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /** 全ユーザー取得（管理者用） */
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /** 新規ユーザー登録（文字列の departId をそのままセット） */
    @PostMapping("/register")
    public ResponseEntity<String> registerUsers(@RequestBody List<UserRegisterDto> dtos) {
        for (UserRegisterDto dto : dtos) {
            User u = new User();
            u.setUserId(dto.getUserId());
            u.setUserName(dto.getUserName());
            u.setPass(dto.getPass());
            u.setMail(dto.getMail());

            // hireDate (yyyy-MM-dd) から年度部分を取り出し
            LocalDate hireDate = LocalDate.parse(dto.getHireDate());
            u.setUserYear(hireDate.getYear());

            // 文字列IDだけをセット
            u.setDepartmentId(dto.getDepartId());

            // 管理者フラグ(0 or 1)
            u.setAdmin(dto.getAdmin() != null ? dto.getAdmin() : 0);

            userService.saveUser(u);
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("登録完了");
    }

    /** ログイン認証 */
    @PostMapping("/login")
    public User login(@RequestParam String userId, @RequestParam String pass) {
        Optional<User> user = userService.login(userId, pass);
        return user.orElse(null);
    }
}