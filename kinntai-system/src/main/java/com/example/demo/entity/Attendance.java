package com.example.demo.entity;

import java.sql.Date;

import lombok.Data;

@Data
public class Attendance {

	private Integer reviewId;
	private Integer restaurantId;
	private String userId;
	private Date visitDate;
	private Integer rating;
	private String comment;
}
