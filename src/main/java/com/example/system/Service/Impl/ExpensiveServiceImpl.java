package com.example.system.Service.Impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.time.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.system.Entity.Employee;
import com.example.system.Entity.Expense;
import com.example.system.Entity.Team;
import com.example.system.Exception.ResourceNotFoundException;
import com.example.system.Repo.EmployeeRepo;
import com.example.system.Repo.ExpenseRepo;
import com.example.system.Repo.TeamRepo;
import com.example.system.Service.ExpenseService;

@Service
public class ExpensiveServiceImpl implements ExpenseService {

    @Autowired
    ExpenseRepo expenseRepo;

    @Autowired
    private TeamRepo teamRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Override
    public List<Expense> getAllExpenses() {

        return expenseRepo.findAll();
    }

    @Override
    public void saveExpense(Expense expense) {
        Team team = expense.getTeam();

        // 如果 team 不為空，則先驗證 team 是否存在
        if (team != null) {
            Team foundTeam = teamRepo.findById(team.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

            // 確認所選擇的員工屬於該 team，若不符合則拋出異常
            Employee employee = employeeRepo.findById(expense.getEmployee().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

            if (!employee.getTeam().getId().equals(foundTeam.getId())) {
                throw new ResourceNotFoundException("Employee does not belong to the selected team.");
            }
        }

        // 處理支出項目的新增或更新
        if (expense.getId() != null) {
            Expense existingExpense = expenseRepo.findById(expense.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        
            existingExpense.setEmployee(expense.getEmployee());
            existingExpense.setAmount(expense.getAmount());
            existingExpense.setCategory(expense.getCategory());
            existingExpense.setDescription(expense.getDescription());
            existingExpense.setPaymentMethod(expense.getPaymentMethod());
            existingExpense.setStatus(expense.getStatus());
            existingExpense.setUpdatedAt(new Date()); // 使用 new Date() 獲取當前時間
        
            expenseRepo.save(existingExpense);
        } else {
            expense.setCreatedAt(new Date()); // 使用 new Date() 獲取當前時間
            expense.setUpdatedAt(new Date()); // 使用 new Date() 獲取當前時間
            expenseRepo.save(expense);
        }
        
    }

    @Override
    public Expense getExpenseById(long id) {

        Optional<Expense> optional = expenseRepo.findById(id);
        Expense expense = null;
        if (optional.isPresent()) {
            expense = optional.get();
        } else {
            throw new RuntimeException(" Expense not found for id :: " + id);
        }
        return expense;
    }

    @Override
    public void deleteExpenseById(long id) {

        this.expenseRepo.deleteById(id);
    }

}
