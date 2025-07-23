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
@RequestMapping("/api/user")  // このコントローラーが処理するリクエストのベースURL
@CrossOrigin  // CORSを許可するアノテーション
@RequiredArgsConstructor  // コンストラクタインジェクションを使用するためのLombokアノテーション
public class UserController {

    private final UserService userService;  // UserServiceのインスタンスを自動的に注入

    /** 
     * 全ユーザー取得（管理者用）
     * @return ユーザーのリスト
     */
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();  // UserServiceを使用して全ユーザーを取得
    }

    /** 
     * 新規ユーザー登録（文字列の departId をそのままセット）
     * @param dtos ユーザー登録情報のリスト
     * @return 登録完了のレスポンス
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUsers(@RequestBody List<UserRegisterDto> dtos) {
        for (UserRegisterDto dto : dtos) {
            User u = new User();  // 新しいUserオブジェクトを作成
            u.setUserId(dto.getUserId());  // ユーザーIDを設定
            u.setUserName(dto.getUserName());  // ユーザー名を設定
            u.setPass(dto.getPass());  // パスワードを設定
            u.setMail(dto.getMail());  // メールアドレスを設定

            // hireDate (yyyy-MM-dd) から年度部分を取り出し
            LocalDate hireDate = LocalDate.parse(dto.getHireDate());  // 入社日をLocalDateに変換
            u.setUserYear(hireDate.getYear());  // 年度を設定

            // 文字列IDだけをセット
            u.setDepartmentId(dto.getDepartId());  // 部署IDを設定

            // 管理者フラグ(0 or 1)
            u.setAdmin(dto.getAdmin() != null ? dto.getAdmin() : 0);  // 管理者フラグを設定（nullの場合は0）

            userService.saveUser(u);  // UserServiceを使用してユーザーを保存
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)  // HTTPステータス201（作成）を設定
                .body("登録完了");  // 登録完了のメッセージを返す
    }

    /** 
     * ログイン認証
     * @param userId ユーザーID
     * @param pass パスワード
     * @return 認証されたユーザー情報
     */
    @PostMapping("/login")
    public User login(@RequestParam String userId, @RequestParam String pass) {
        Optional<User> user = userService.login(userId, pass);  // UserServiceを使用してログイン認証
        return user.orElse(null);  // ユーザーが見つからない場合はnullを返す
    }
}