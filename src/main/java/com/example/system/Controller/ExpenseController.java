package com.example.system.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.system.Entity.Expense;
import com.example.system.Exception.ResourceNotFoundException;
import com.example.system.Repo.TeamRepo;
import com.example.system.Service.EmployeeService;
import com.example.system.Service.ExpenseService;

import java.util.List;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TeamRepo teamRepo;

    // 顯示支出列表
    @GetMapping
    public String listExpenses(Model model) {
        List<Expense> expenses = expenseService.getAllExpenses();
        model.addAttribute("expenses", expenses);
        return "expense-list"; // 返回顯示支出列表的 Thymeleaf 模板
    }

    // 顯示新增支出表單
    @GetMapping("/new")
    public String showCreateExpenseForm(Model model) {
        Expense expense = new Expense();
        List<String> status = List.of("待審核", "已付款", "取消");
        List<String> category = List.of("文具", "伙食");
        List<String> payMethod = List.of("信用卡", "現金");

        model.addAttribute("expense", expense);
        model.addAttribute("teams", teamRepo.findAll());
        model.addAttribute("status", status);
        model.addAttribute("payMethod", payMethod);
        model.addAttribute("category", category);
        model.addAttribute("employees", employeeService.getAllEmployees());

        return "expense-form"; // 返回支出表單頁面
    }

    // 處理新增或更新支出請求
    @PostMapping("/save")
    public String saveExpense(@ModelAttribute("expense") Expense expense, Model model) {
        try {
            // 嘗試保存支出
            expenseService.saveExpense(expense);
            return "redirect:/expenses"; // 保存成功後重定向到支出列表頁
        } catch (ResourceNotFoundException ex) {
            // 捕捉到 ResourceNotFoundException 時
            List<String> status = List.of("待審核", "已付款", "取消");
            List<String> category = List.of("文具", "伙食");
            List<String> payMethod = List.of("信用卡", "現金");

            // 將原始表單數據、選項以及錯誤訊息傳遞給前端
            model.addAttribute("expense", expense); // 保留用戶輸入的支出數據
            model.addAttribute("teams", teamRepo.findAll());
            model.addAttribute("status", status);
            model.addAttribute("payMethod", payMethod);
            model.addAttribute("category", category);
            model.addAttribute("employees", employeeService.getAllEmployees());
            model.addAttribute("errorMessage", ex.getMessage()); // 傳遞錯誤訊息

            return "expense-form"; // 返回表單頁面
        }
    }

    // 顯示編輯支出表單
    @GetMapping("/edit/{id}")
    public String showUpdateExpenseForm(@PathVariable(value = "id") long id, Model model) {
        Expense expense = expenseService.getExpenseById(id);
        List<String> status = List.of("待審核", "已付款", "取消");
        List<String> category = List.of("文具", "伙食");
        List<String> payMethod = List.of("信用卡", "現金");

        model.addAttribute("expense", expense);
        model.addAttribute("teams", teamRepo.findAll());
        model.addAttribute("status", status);
        model.addAttribute("payMethod", payMethod);
        model.addAttribute("category", category);
        model.addAttribute("employees", employeeService.getAllEmployees());

        return "expense-form"; // 返回支出編輯表單頁面
    }

    // 刪除支出
    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable(value = "id") long id) {
        expenseService.deleteExpenseById(id);
        return "redirect:/expenses"; // 刪除成功後重定向到支出列表頁
    }
}
