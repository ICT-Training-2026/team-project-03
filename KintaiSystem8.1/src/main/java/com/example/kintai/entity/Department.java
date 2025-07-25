// src/main/java/com/example/kintai/entity/Department.java
package com.example.kintai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "department")
public class Department {

    @Id
    @Column(name = "depart_id")
    private String departId;

    @Column(name = "depart_name")
    private String departName;

    // --- getters/setters ---
    public String getDepartId() { return departId; }
    public void setDepartId(String departId) { this.departId = departId; }

    public String getDepartName() { return departName; }
    public void setDepartName(String departName) { this.departName = departName; }
}