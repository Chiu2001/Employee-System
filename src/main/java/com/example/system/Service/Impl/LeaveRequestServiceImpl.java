package com.example.system.Service.Impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.system.Entity.Employee;
import com.example.system.Entity.LeaveRequest;
import com.example.system.Repo.LeaveRequestRepo;
import com.example.system.Service.LeaveRequestService;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService{

    @Autowired
    private LeaveRequestRepo leaveRequestRepo;

    @Override
    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
        System.out.println(leaveRequest.getEmployee());
        System.out.println(leaveRequest.getTeam());
        System.out.println(leaveRequest.getLeaveType());
        leaveRequest.setCreatedAt(LocalDateTime.now());
        return leaveRequestRepo.save(leaveRequest);
    }

    @Override
    public List<LeaveRequest> getLeaveRequestsByEmployee(Employee employee) {
        return leaveRequestRepo.findByEmployee(employee);
    }

}
