package com.example.budgettrackerv1.service;

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
            LOGGER.error("Could not save expense. Error: {}",  e.getLocalizedMessage(), e);
            return false;
        }
    }

    public boolean update(Expense expense) {
        if (!this.EXPENSE_REPOSITORY.existsById(expense.getId())) {
            LOGGER.debug("Could not find expense with ID {}.", expense.getId());
            return false;
        }
        try {
            this.EXPENSE_REPOSITORY.save(expense);
            return true;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Could not update expense with ID {}. Error: {}", expense.getAmount(), e.getLocalizedMessage(), e);
            return false;
        }
    }

    public boolean delete(int id) {
        if (!this.EXPENSE_REPOSITORY.existsById(id)) {
            LOGGER.debug("Could not find expense with ID {}.", id);
            return false;
        }
        try {
            this.EXPENSE_REPOSITORY.findById(id).ifPresent(this.EXPENSE_REPOSITORY::delete);
            return true;
        } catch (Exception e) {
            LOGGER.error("Could not delete expense with ID {}. Error: {}", id, e.getLocalizedMessage(), e);
            return false;
        }
    }
}
