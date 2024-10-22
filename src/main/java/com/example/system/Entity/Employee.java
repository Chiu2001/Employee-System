package com.example.system.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String phone;

    @Column
    private String gender;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "rank_id", nullable = false)
    private Rank rank;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column
    private String role;

    @Column
    private String status;


    @Column(name = "join_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date joinDate;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses = new ArrayList<>(); // 定義員工與多個支出的關聯

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LeaveRequest> leaveRequest = new ArrayList<>();

    // Constructors
    public Employee(Long id, String name, String email, String password, String phone, String gender, Rank rank,
                    Team team, String role, String status, Date joinDate, List<Expense> expenses, List<LeaveRequest> leaveRequest) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.gender = gender;
        this.rank = rank;
        this.team = team;
        this.role = role;
        this.status = status;
        this.joinDate = joinDate;
        this.expenses = expenses;
        this.leaveRequest = leaveRequest;
    }

    public Employee() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public List<LeaveRequest> getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(List<LeaveRequest> leaveRequest) {
        this.leaveRequest = leaveRequest;
    }
}
