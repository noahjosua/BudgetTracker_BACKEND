package com.team7.budgettracker.service;

import com.team7.budgettracker.exception.EntryNotFoundException;
import com.team7.budgettracker.model.Expense;
import com.team7.budgettracker.repository.ExpenseRepository;
import com.team7.budgettracker.exception.EntryNotProcessedException;
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

    /**
     * Retrieves a list of expenses within the specified date range.
     *
     * @param start the start date of the range
     * @param end   the end date of the range
     * @return an Optional containing the list of expenses if found, otherwise an empty Optional
     */
    public Optional<List<Expense>> getByDate(LocalDate start, LocalDate end) {
        return this.EXPENSE_REPOSITORY.findAllByLocalDatePlannedBetween(start, end);
    }

    /**
     * Saves the given expense entity.
     *
     * @param expense the expense entity to be saved
     * @return true if the expense is successfully saved, otherwise false
     * @throws EntryNotProcessedException if there is an error during the save operation
     */
    public boolean save(Expense expense) {
        try {
            this.EXPENSE_REPOSITORY.save(expense);
            return true;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Could not save expense. Error: {}", e.getLocalizedMessage(), e);
            throw new EntryNotProcessedException(String.format("Could not save expense. Error: %s", e.getLocalizedMessage()));
        }
    }

    /**
     * Updates the given expense entity.
     *
     * @param expense the updated expense entity
     * @return true if the expense is successfully updated, otherwise false
     * @throws EntryNotFoundException     if the expense with the specified ID is not found
     * @throws EntryNotProcessedException if there is an error during the update operation
     */
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

    /**
     * Deletes the expense with the specified ID.
     *
     * @param id the ID of the expense to be deleted
     * @return true if the expense is successfully deleted, otherwise false
     * @throws EntryNotFoundException     if the expense with the specified ID is not found
     * @throws EntryNotProcessedException if there is an error during the delete operation
     */
    public boolean delete(int id) {
        try {
            Optional<Expense> expense = this.EXPENSE_REPOSITORY.findById(id);
            if (expense.isPresent()) {
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
