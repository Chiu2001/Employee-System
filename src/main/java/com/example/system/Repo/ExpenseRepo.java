package com.example.system.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.system.Entity.Expense;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, Long>{

    Optional<Expense> findById(Long id);
}
