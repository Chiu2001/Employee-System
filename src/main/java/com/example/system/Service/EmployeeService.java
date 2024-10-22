package com.example.system.Service;

import java.util.List;

import com.example.system.Entity.Employee;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    void saveEmployee(Employee employee);

    Employee getEmployeeById(long id);

    void deleteEmployeeById(long id);

    Employee login(String email, String password);

    List<Employee> searchEmployees(String username, String team);

    Employee findByEmailAndPassword(String email, String password);

    void updateStatus(Long employeeId, String status);
}
