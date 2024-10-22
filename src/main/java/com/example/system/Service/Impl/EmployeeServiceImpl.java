package com.example.system.Service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.system.Entity.Employee;
import com.example.system.Exception.ResourceNotFoundException;
import com.example.system.Repo.EmployeeRepo;
import com.example.system.Service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    @Override
    public void saveEmployee(Employee employee) {
        if (employee.getId() != null) {
            Employee existingEmployee = employeeRepo.findById(employee.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

            existingEmployee.setName(employee.getName());
            existingEmployee.setEmail(employee.getEmail());
            existingEmployee.setPhone(employee.getPhone());
            existingEmployee.setRole(employee.getRole());
            existingEmployee.setRank(employee.getRank());
            existingEmployee.setTeam(employee.getTeam());
            existingEmployee.setJoinDate(employee.getJoinDate());

            employeeRepo.save(existingEmployee);
        } else {
            String encodedPassword = passwordEncoder.encode(employee.getPassword());
            employee.setPassword(encodedPassword);
            employee.setStatus("Out");
            employeeRepo.save(employee);
        }
    }

    @Override
    public Employee getEmployeeById(long id) {
        Optional<Employee> optional = employeeRepo.findById(id);
        Employee employee = null;
        if (optional.isPresent()) {
            employee = optional.get();
        } else {
            throw new RuntimeException(" Employee not found for id :: " + id);
        }
        return employee;
    }

    @Override
    public void deleteEmployeeById(long id) {
        this.employeeRepo.deleteById(id);
    }

    @Override
    public Employee login(String email, String password) {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );

        // 查詢用戶
        return employeeRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public List<Employee> searchEmployees(String username, String team) {
        return employeeRepo.searchEmployees(username, team);
    }

    // 查找员工通过 email 和 password
    @Override
    public Employee findByEmailAndPassword(String email, String password) {
        Employee employee = employeeRepo.findUserByEmail(email);
        if (employee != null && passwordEncoder.matches(password, employee.getPassword())) {
            return employee;
        }
        return null; // 返回 null 表示驗證失敗
    }

    // 更新打卡状态
    @Override
    public void updateStatus(Long employeeId, String status) {
        System.out.println("Id123:" + employeeId);
        Employee employee = employeeRepo.findById(employeeId).orElseThrow();
        // 假设员工实体有一个打卡状态字段
        employee.setStatus(status);
        employeeRepo.save(employee);
    }
}
