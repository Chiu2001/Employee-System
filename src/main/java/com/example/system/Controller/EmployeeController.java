package com.example.system.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.system.Entity.Employee;
import com.example.system.Entity.Rank;
import com.example.system.Entity.Team;
import com.example.system.Exception.ResourceNotFoundException;
import com.example.system.Repo.RankRepo;
import com.example.system.Repo.TeamRepo;
import com.example.system.Service.EmployeeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RankRepo rankRepo;

    @Autowired
    private TeamRepo teamRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginPage(Model model, HttpServletRequest request) {
        // 從請求中獲取 session
        HttpSession session = request.getSession();

        // 檢查 session 中是否存在錯誤消息
        String errorMessage = (String) session.getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            // 清除 session 中的錯誤消息
            session.removeAttribute("errorMessage");
        }

        // 初始化一個空的 Employee 用於表單綁定
        model.addAttribute("employee", new Employee());
        return "login"; // 返回登入頁面
    }

    @GetMapping("/employee-list")
    public String viewHomePage(Model model,
            @RequestParam(value = "username", required = false, defaultValue = "") String username,
            @RequestParam(value = "team", required = false, defaultValue = "All") String team) {
        model.addAttribute("listEmployees", employeeService.searchEmployees(username, team));
        model.addAttribute("teams", teamRepo.findAll()); // 確保有一個 service 來獲取所有團隊
        return "employee-list";
    }

    @GetMapping("/showNewEmployeeForm")
    public String showNewEmployeeForm(Model model) {
        Employee employee = new Employee();

        // 獲取所有 Rank 和 Team 的資料，供表單選擇
        List<Rank> ranks = rankRepo.findAll(); // 假設有 rankRepo
        List<Team> teams = teamRepo.findAll(); // 假設有 teamRepo
        List<String> roles = List.of("Admin", "User");

        // 傳遞資料給前端表單
        model.addAttribute("ranks", ranks); // 傳遞 Rank 資料
        model.addAttribute("teams", teams); // 傳遞 Team 資料
        model.addAttribute("roles", roles); // 傳遞角色資料
        model.addAttribute("employee", employee); // 空的 Employee 物件，用於表單綁定

        return "new_employee"; // 返回前端模板
    }

    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        // 獲取 Rank 和 Team 的實體
        System.out.println("--------" + employee.getId());
        Rank rank = rankRepo.findById(employee.getRank().getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Rank not found with ID: " + employee.getRank().getId()));
        Team team = teamRepo.findById(employee.getTeam().getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Team not found with ID: " + employee.getTeam().getId()));

        // 設置 Rank 和 Team
        employee.setRank(rank);
        employee.setTeam(team);
        employeeService.saveEmployee(employee);
        return "redirect:/employee-list";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) {

        Employee employee = employeeService.getEmployeeById(id);
        // 獲取所有 Rank 和 Team 的資料，供表單選擇
        List<Rank> ranks = rankRepo.findAll(); // 假設有 rankRepo
        List<Team> teams = teamRepo.findAll(); // 假設有 teamRepo
        List<String> roles = List.of("Admin", "User");

        // 傳遞資料給前端表單
        model.addAttribute("ranks", ranks); // 傳遞 Rank 資料
        model.addAttribute("teams", teams); // 傳遞 Team 資料
        model.addAttribute("roles", roles); // 傳遞角色資料
        model.addAttribute("employee", employee);
        return "update_employee";
    }

    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable(value = "id") long id) {

        employeeService.deleteEmployeeById(id);
        return "redirect:/employee-list";
    }

    @GetMapping("/clock-in-out")
    public String clockInOut(Model model) {
        // 初始化一個空的 Employee 用於表單綁定
        model.addAttribute("employee", new Employee());
        return "checkIn-checkOut";
    }

    @PostMapping("/clock-in-out")
    public String clockInOut(@RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("status") String status,
            Model model) {
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        // 验证 email 和 password
        Employee employee = employeeService.findByEmailAndPassword(email, password);

        if (employee == null) {
            model.addAttribute("error", "Invalid email or password");
            return "checkIn-checkOut";
        }

        System.out.println("Id:" + employee.getId());

        // 更新员工的打卡状态
        employeeService.updateStatus(employee.getId(), status);

        // 重定向回员工列表页
        return "redirect:/employee-list";
    }
}
