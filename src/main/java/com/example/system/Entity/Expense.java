package com.example.system.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 自動增長的主鍵

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee; // 負責人 (必須關聯一位員工)

    @Column(name = "category", nullable = false, length = 100)
    private String category; // 支出類別

    @Column(name = "amount", nullable = false)
    private BigDecimal amount; // 支出金額

    @Column(name = "description", length = 255)
    private String description; // 支出描述

    @Column(name = "expense_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expenseDate; // 支出日期

    @Column(name = "payment_method", length = 50)
    private String paymentMethod; // 支付方式

    @Column(name = "status", length = 50)
    private String status = "待審核"; // 支出狀態，預設為 "待審核"

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt; // 創建時間

    @Column(name = "updated_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt; // 更新時間

    public Expense() {
        this.createdAt = new Date(); // 使用當前日期作為創建時間
        this.updatedAt = new Date(); // 使用當前日期作為更新時間
    }

     public Expense(Long id, Team team, Employee employee, String category, BigDecimal amount, String description,
            Date expenseDate, String paymentMethod, String status, Date createdAt, Date updatedAt) {
        this.id = id;
        this.team = team;
        this.employee = employee;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.expenseDate = expenseDate;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}


