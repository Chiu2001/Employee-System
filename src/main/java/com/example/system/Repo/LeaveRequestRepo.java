package com.example.system.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.system.Entity.Employee;
import com.example.system.Entity.LeaveRequest;
import com.example.system.Entity.Team;

@Repository
public interface LeaveRequestRepo extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployee(Employee employee);

    List<LeaveRequest> findByTeam(Team team);

    LeaveRequest getLeaveRequestById(Long id);

}