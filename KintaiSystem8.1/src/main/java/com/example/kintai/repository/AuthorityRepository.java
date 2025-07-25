package com.example.kintai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kintai.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
}