package com.example.kintai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.kintai.entity.Authority;
import com.example.kintai.repository.AuthorityRepository;

@Service
public class AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;  // AuthorityRepositoryのインスタンスを自動的に注入

    /** 
     * 全ての権限を取得するメソッド
     * 
     * @return 権限のリスト
     */
    public List<Authority> getAllAuthorities() {
        return authorityRepository.findAll();  // リポジトリを使用して全ての権限を取得
    }
}