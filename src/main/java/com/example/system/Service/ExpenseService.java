package com.example.system.Service;

import com.example.system.Entity.Expense;

import java.util.List;

public interface ExpenseService {

    List<Expense> getAllExpenses();

    void saveExpense(Expense expense);

    Expense getExpenseById(long id);

    void deleteExpenseById(long id);
}
