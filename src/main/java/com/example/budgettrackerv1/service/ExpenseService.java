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

    public Optional<List<Expense>> getExpenses() {
        return Optional.of(this.EXPENSE_REPOSITORY.findAll());
    }

    public Optional<Expense> getById(int id) {
        try {
            return this.EXPENSE_REPOSITORY.findById(id);
        } catch (IllegalArgumentException e) {
            System.out.printf("[%s] Could not get expense with ID %d.%n", e.getLocalizedMessage(), id);
            return Optional.empty();
        }
    }

    public Optional<List<Expense>> getByDate(LocalDate start, LocalDate end) {
        return this.EXPENSE_REPOSITORY.findAllByLocalDatePlannedBetween(start, end);
    }

    public boolean save(Expense expense) {
        try {
            this.EXPENSE_REPOSITORY.save(expense);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.printf("[%s] Could not save expense with ID %d.%n", e.getLocalizedMessage(), expense.getId());
            return false;
        }
    }

    // TODO Errorhandling
    public void update(Expense expense) {
        if (!this.EXPENSE_REPOSITORY.existsById(expense.getId())) {
            throw new RuntimeException("Expense not found");
        }
        this.EXPENSE_REPOSITORY.save(expense);
    }

    // TODO Errorhandling
    public void delete(int id) {
        if (!this.EXPENSE_REPOSITORY.existsById(id)) {
            throw new RuntimeException("Expense not found");
        }
        this.EXPENSE_REPOSITORY.findById(id).ifPresent(this.EXPENSE_REPOSITORY::delete);
    }
}
