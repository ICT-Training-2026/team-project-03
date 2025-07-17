package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	/*--- 完了後のリダイレクト先 ---*/
	@GetMapping("/complete")
	private String complete() {
		return "complete";
	}
}