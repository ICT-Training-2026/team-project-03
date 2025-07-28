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

    /** 新規ユーザー登録 */
    @PostMapping("/register")
    public ResponseEntity<String> registerUsers(@RequestBody List<UserRegisterDto> dtos) {
        for (UserRegisterDto dto : dtos) {
            User u = new User();
            u.setUserId(dto.getUserId());
            u.setUserName(dto.getUserName());
            u.setMail(dto.getMail());
            // 入社年月日から年度を計算
            LocalDate hireDate = LocalDate.parse(dto.getHireDate());
            u.setUserYear(hireDate.getYear());
            // 部署IDをそのままセット
            u.setDepartmentId(dto.getDepartId());
            // 管理者フラグ（0 or 1）
            u.setAdmin(dto.getAdmin() != null ? dto.getAdmin() : 0);
            // パスワードはサービス層でハッシュ化してセット
            userService.saveUser(u, dto.getPass());
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("登録完了");
    }

    /** ログイン認証 */
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String userId,
                                      @RequestParam String pass) {
        Optional<User> opt = userService.login(userId, pass);
        if (opt.isEmpty()) {
            // 認証失敗なら 401 Unauthorized を返す
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // 認証成功ならユーザー情報をそのまま JSON で返す
        return ResponseEntity.ok(opt.get());
    }
}