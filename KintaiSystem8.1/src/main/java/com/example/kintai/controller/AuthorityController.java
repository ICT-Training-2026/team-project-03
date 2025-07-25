package com.example.kintai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kintai.entity.Authority;
import com.example.kintai.service.AuthorityService;

@RestController
@RequestMapping("/api/authority")
@CrossOrigin
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;

    @GetMapping("/all")
    public List<Authority> getAllAuthorities() {
        return authorityService.getAllAuthorities();
    }
}