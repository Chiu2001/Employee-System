package com.example.system.Service;

import java.util.List;

import com.example.system.Entity.Employee;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    void addEmployee(Employee employee);
}
