package com.example.budgettrackerv1.service;

import com.example.budgettrackerv1.model.Expense;
import com.example.budgettrackerv1.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    private final ExpenseRepository EXPENSE_REPOSITORY;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.EXPENSE_REPOSITORY = expenseRepository;
    }

    public Optional<List<Expense>> getByDate(LocalDate start, LocalDate end) {
        return this.EXPENSE_REPOSITORY.findAllByLocalDatePlannedBetween(start, end);
    }

    public boolean save(Expense expense) {
        try {
            this.EXPENSE_REPOSITORY.save(expense);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.printf("[%s] Could not save expense.%n", e.getLocalizedMessage());
            return false;
        }
    }

    public boolean update(Expense expense) {
        if (!this.EXPENSE_REPOSITORY.existsById(expense.getId())) {
            System.out.printf("Could not find expense with ID %d.%n", expense.getId());
            return false;
        }
        try {
            this.EXPENSE_REPOSITORY.save(expense);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.printf("[%s] Could not update expense with ID %d.%n", e.getLocalizedMessage(), expense.getId());
            return false;
        }
    }

    public boolean delete(int id) {
        if (!this.EXPENSE_REPOSITORY.existsById(id)) {
            System.out.printf("Could not find expense with ID %d.%n", id);
            return false;
        }
        try {
            this.EXPENSE_REPOSITORY.findById(id).ifPresent(this.EXPENSE_REPOSITORY::delete);
            return true;
        } catch (Exception e) {
            System.out.printf("[%s] Could not delete expense with ID %d.%n", e.getLocalizedMessage(), id);
            return false;
        }
    }
}
