package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Attendance;
import com.example.demo.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KintaiService implements LoginService {

	private final ReviewRepository repository;
	
	@Override
	public void edit(Attendance review) {
		
		repository.update(review);
		
	}

}
