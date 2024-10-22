package com.example.system.Entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Employee> employees;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Expense> expenses;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LeaveRequest> leaveRequest = new ArrayList<>() ;
    
    public Team(){
        
    }

     public Team(Long id, String name, String description, Set<Employee> employees, Set<Expense> expenses,  List<LeaveRequest> leaveRequest) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.employees = employees;
        this.expenses = expenses;
        this.leaveRequest = leaveRequest;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public Set<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    public List<LeaveRequest> getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(List<LeaveRequest> leaveRequest) {
        this.leaveRequest = leaveRequest;
    }
}

