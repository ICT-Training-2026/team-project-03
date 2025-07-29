package com.example.kintai.controller;

import java.util.List;

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
import com.example.kintai.repository.UserRepository;
import com.example.kintai.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    /** 全ユーザー取得（管理者用） */
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /** 新規ユーザー登録 */
    @PostMapping("/register")
    public ResponseEntity<String> registerUsers(@RequestBody List<UserRegisterDto> dtos) {
        // ① 受け取ったIDの重複チェック
        for (UserRegisterDto dto : dtos) {
            if (userRepository.existsById(dto.getUserId())) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("ユーザーID " + dto.getUserId() + " は既に存在します。");
            }
        }
        // ② 重複なければ登録処理
        for (UserRegisterDto dto : dtos) {
            User u = new User();
            u.setUserId(dto.getUserId());
            u.setUserName(dto.getUserName());
            u.setMail(dto.getMail());
            // 入社年月日から年度を計算
            u.setUserYear(java.time.LocalDate.parse(dto.getHireDate()).getYear());
            // 部署IDをそのままセット
            u.setDepartmentId(dto.getDepartId());
            // 管理者フラグ（0 or 1）
            u.setAdmin(dto.getAdmin() != null ? dto.getAdmin() : 0);
            // パスワードはサービス層でハッシュ化して保存
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
        return userService.login(userId, pass)
                          .map(ResponseEntity::ok)
                          .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}