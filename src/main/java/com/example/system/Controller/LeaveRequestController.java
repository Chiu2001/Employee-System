package com.example.system.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.system.Entity.Employee;
import com.example.system.Entity.LeaveRequest;
import com.example.system.Service.EmployeeService;
import com.example.system.Service.LeaveRequestService;
import com.example.system.Exception.ResourceNotFoundException;
import com.example.system.Repo.LeaveRequestRepo;
import com.example.system.Repo.EmployeeRepo;

@Controller
@RequestMapping("/leave-requests")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @Autowired
    private LeaveRequestRepo leaveRequestRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    // 顯示請假申請表單
    @GetMapping("/create")
    public String showLeaveRequestForm(Model model) {
        model.addAttribute("leaveRequest", new LeaveRequest());
        // 可以添加更多資料，例如 leaveType 選項
        return "leave-request-form"; // Thymeleaf 模板名稱
    }

    // 處理請假申請
    @PostMapping("/submit")
    public String submitLeaveRequest(@ModelAttribute("leaveRequest") LeaveRequest leaveRequest,
            Principal principal, RedirectAttributes redirectAttributes) {

        // 使用 findByEmail 查詢 Employee 並檢查是否存在
        Optional<Employee> optionalEmployee = employeeRepo.findByEmail(principal.getName());

        // 檢查 Employee 是否存在
        if (optionalEmployee.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "無法找到登錄用戶的資訊");
            return "redirect:/leave-requests/create";
        }

        // 從 Optional 中獲取 Employee 對象
        Employee employee = optionalEmployee.get();
        leaveRequest.setEmployee(employee);
        leaveRequest.setStatus("pending");
        leaveRequest.setTeam(employee.getTeam()); // 設置 team
        leaveRequest.setCreatedAt(LocalDateTime.now());
        leaveRequestRepo.save(leaveRequest);

        redirectAttributes.addFlashAttribute("message", "請假申請提交成功！");
        return "redirect:/leave-requests/list";
    }

    // 列出所有請假申請
    @GetMapping("/list")
    public String listLeaveRequests(Model model, Principal principal) {
        // 使用 findByEmail 查詢 Employee
        Optional<Employee> optionalEmployee = employeeRepo.findByEmail(principal.getName());

        // 檢查 Employee 是否存在
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get(); // 從 Optional 中獲取 Employee 對象

            List<LeaveRequest> leaveRequests;
            if (employee.getRole().equals("Admin")) {
                leaveRequests = leaveRequestRepo.findByTeam(employee.getTeam());
            } else {
                leaveRequests = leaveRequestService.getLeaveRequestsByEmployee(employee);
            }

            model.addAttribute("leaveRequests", leaveRequests);
            model.addAttribute("isAdmin", employee.getRole().equals("Admin")); // 傳遞是否為 ADMIN 的信息到前端
        } else {
            model.addAttribute("leaveRequests", new ArrayList<>()); // 設置一個空列表
            model.addAttribute("error", "無法找到登錄用戶的資訊");
        }

        return "leave-request-list"; // Thymeleaf 模板名稱
    }

    // 審批請假申請（僅管理員或 HR 可用）
    @PostMapping("/approve/{id}")
    public String approveLeaveRequest(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        LeaveRequest leaveRequest = leaveRequestRepo.getLeaveRequestById(id);
        if (leaveRequest != null) {
            leaveRequest.setStatus("approved");
            leaveRequestRepo.save(leaveRequest);
            redirectAttributes.addFlashAttribute("message", "請假申請已審批通過！");
        } else {
            redirectAttributes.addFlashAttribute("error", "請假申請未找到！");
        }
        return "redirect:/leave-requests/list";
    }

    // 拒絕請假申請（僅管理員或 HR 可用）
    @PostMapping("/reject/{id}")
    public String rejectLeaveRequest(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        LeaveRequest leaveRequest = leaveRequestRepo.getLeaveRequestById(id);
        if (leaveRequest != null) {
            leaveRequest.setStatus("rejected");
            leaveRequestRepo.save(leaveRequest);
            redirectAttributes.addFlashAttribute("message", "請假申請已拒絕！");
        } else {
            redirectAttributes.addFlashAttribute("error", "請假申請未找到！");
        }
        return "redirect:/leave-requests/list";
    }
}
