package com.example.system.Service;

import java.util.List;

import com.example.system.Entity.Employee;
import com.example.system.Entity.LeaveRequest;

public interface LeaveRequestService {

    LeaveRequest createLeaveRequest(LeaveRequest leaveRequest);

    List<LeaveRequest> getLeaveRequestsByEmployee(Employee employee);

}
