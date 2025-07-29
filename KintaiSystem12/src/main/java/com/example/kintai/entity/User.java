package com.example.kintai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee")
public class User {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "pass")
    private String pass;

    @Column(name = "mail")
    private String mail;

    @Column(name = "depart_id")
    private String departmentId;

    @Column(name = "user_year")
    private int userYear;

    @Column(name = "admin")
    private Integer admin;

    // --- getters / setters ---
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPass() { return pass; }
    public void setPass(String pass) { this.pass = pass; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    public int getUserYear() { return userYear; }
    public void setUserYear(int userYear) { this.userYear = userYear; }

    public Integer getAdmin() { return admin; }
    public void setAdmin(Integer admin) { this.admin = admin; }
	public void setDepartment(Department dept) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}