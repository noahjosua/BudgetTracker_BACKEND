package com.example.budgettrackerv1.service;

import com.example.budgettrackerv1.exception.EntryNotFoundException;
import com.example.budgettrackerv1.exception.EntryNotProcessedException;
import com.example.budgettrackerv1.model.Expense;
import com.example.budgettrackerv1.repository.ExpenseRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("LoggingSimilarMessage")
@Service
public class ExpenseService {

    private final ExpenseRepository EXPENSE_REPOSITORY;

    private static final Logger LOGGER = LogManager.getLogger(ExpenseService.class);

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
            LOGGER.error("Could not save expense. Error: {}", e.getLocalizedMessage(), e);
            throw new EntryNotProcessedException(String.format("Could not save expense. Error: %s", e.getLocalizedMessage()));
        }
    }

    public boolean update(Expense expense) {
        try {
            Optional<Expense> expenseOptional = this.EXPENSE_REPOSITORY.findById(expense.getId());
            if (expenseOptional.isPresent()) {
                this.delete(expense.getId());
                this.EXPENSE_REPOSITORY.save(expense);
                return true;
            }
            throw new EntryNotFoundException("Expense not found. Could not update expense.");
        } catch (IllegalArgumentException e) {
            LOGGER.error("Could not update expense. Error: {}", e.getLocalizedMessage(), e);
            throw new EntryNotProcessedException(String.format("Could not update expense. Error: %s", e.getLocalizedMessage()));
        }
    }

    public boolean delete(int id) {
        try {
            Optional<Expense> expense = this.EXPENSE_REPOSITORY.findById(id);
            if(expense.isPresent()) {
                this.EXPENSE_REPOSITORY.delete(expense.get());
                return true;
            }
            LOGGER.info("Expense not found. Could not delete expense.");
            throw new EntryNotFoundException("Expense not found. Could not delete expense.");
        } catch (IllegalArgumentException | NullPointerException e) {
            LOGGER.error("Could not delete expense with ID {}. Error: {}", id, e.getLocalizedMessage(), e);
            throw new EntryNotProcessedException(String.format("Could not delete expense. Error: %s", e.getLocalizedMessage()));
        }
    }
}
