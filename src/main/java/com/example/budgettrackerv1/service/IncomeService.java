package com.example.budgettrackerv1.service;

import com.example.budgettrackerv1.exception.EntryNotFoundException;
import com.example.budgettrackerv1.exception.EntryNotProcessedException;
import com.example.budgettrackerv1.model.Income;
import com.example.budgettrackerv1.repository.IncomeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("LoggingSimilarMessage")
@Service
public class IncomeService {

    private final IncomeRepository INCOME_REPOSITORY;

    private static final Logger LOGGER = LogManager.getLogger(IncomeService.class);

    @Autowired
    public IncomeService(IncomeRepository incomeRepository) {
        this.INCOME_REPOSITORY = incomeRepository;
    }

    /**
     * Retrieves a list of incomes within the specified date range.
     *
     * @param start the start date of the range
     * @param end   the end date of the range
     * @return an Optional containing the list of incomes if found, otherwise an empty Optional
     */
    public Optional<List<Income>> getByDate(LocalDate start, LocalDate end) {
        return this.INCOME_REPOSITORY.findAllByLocalDatePlannedBetween(start, end);
    }

    /**
     * Saves the given income entity.
     *
     * @param income the income entity to be saved
     * @return true if the income is successfully saved, otherwise false
     * @throws EntryNotProcessedException if there is an error during the save operation
     */
    public boolean save(Income income) {
        try {
            this.INCOME_REPOSITORY.save(income);
            return true;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Could not save income. Error: {}", e.getLocalizedMessage(), e);
            throw new EntryNotProcessedException(String.format("Could not save income. Error: %s", e.getLocalizedMessage()));
        }
    }

    /**
     * Updates the given income entity.
     *
     * @param income the updated income entity
     * @return true if the income is successfully updated, otherwise false
     * @throws EntryNotFoundException     if the income with the specified ID is not found
     * @throws EntryNotProcessedException if there is an error during the update operation
     */
    public boolean update(Income income) {
        try {
            Optional<Income> incomeOptional = this.INCOME_REPOSITORY.findById(income.getId());
            if (incomeOptional.isPresent()) {
                this.delete(income.getId());
                this.INCOME_REPOSITORY.save(income);
                return true;
            }
            throw new EntryNotFoundException("Income not found. Could not update income.");
        } catch (IllegalArgumentException e) {
            LOGGER.error("Could not update income. Error: {}", e.getLocalizedMessage(), e);
            throw new EntryNotProcessedException(String.format("Could not update income. Error: %s", e.getLocalizedMessage()));
        }
    }

    /**
     * Deletes the income with the specified ID.
     *
     * @param id the ID of the income to be deleted
     * @return true if the income is successfully deleted, otherwise false
     * @throws EntryNotFoundException     if the income with the specified ID is not found
     * @throws EntryNotProcessedException if there is an error during the delete operation
     */
    public boolean delete(int id) {
        try {
            Optional<Income> income = this.INCOME_REPOSITORY.findById(id);
            if (income.isPresent()) {
                this.INCOME_REPOSITORY.delete(income.get());
                return true;
            }
            LOGGER.info("Income not found. Could not delete income.");
            throw new EntryNotFoundException("Income not found. Could not delete income.");
        } catch (IllegalArgumentException | NullPointerException e) {
            LOGGER.error("Could not delete income with ID {}. Error: {}", id, e.getLocalizedMessage(), e);
            throw new EntryNotProcessedException(String.format("Could not delete income. Error: %s", e.getLocalizedMessage()));
        }
    }
}
