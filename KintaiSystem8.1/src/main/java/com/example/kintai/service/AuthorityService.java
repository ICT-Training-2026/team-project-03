package com.example.kintai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.kintai.entity.Authority;
import com.example.kintai.repository.AuthorityRepository;

@Service
public class AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    public List<Authority> getAllAuthorities() {
        return authorityRepository.findAll();
    }
}