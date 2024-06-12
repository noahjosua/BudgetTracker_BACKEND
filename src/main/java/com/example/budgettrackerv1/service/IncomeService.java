package com.example.budgettrackerv1.service;

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

    public Optional<List<Income>> getByDate(LocalDate start, LocalDate end) {
        return this.INCOME_REPOSITORY.findAllByLocalDatePlannedBetween(start, end);
    }

    public boolean save(Income income) {
        try {
            this.INCOME_REPOSITORY.save(income);
            return true;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Could not save income. Error: {}",  e.getLocalizedMessage(), e);
            return false;
        }
    }

    public boolean update(Income income) {
        if (!this.INCOME_REPOSITORY.existsById(income.getId())) {
            LOGGER.debug("Could not find income with ID {}.", income.getId());
            return false;
        }
        try {
            this.INCOME_REPOSITORY.save(income);
            return true;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Could not update income with ID {}. Error: {}", income.getAmount(), e.getLocalizedMessage(), e);
            return false;
        }
    }

    public boolean delete(int id) {
        if (!this.INCOME_REPOSITORY.existsById(id)) {
            LOGGER.debug("Could not find income with ID {}.", id);
            return false;
        }
        try {
            this.INCOME_REPOSITORY.findById(id).ifPresent(this.INCOME_REPOSITORY::delete);
            return true;
        } catch (Exception e) {
            LOGGER.error("Could not delete income with ID {}. Error: {}", id, e.getLocalizedMessage(), e);
            return false;
        }
    }
}
