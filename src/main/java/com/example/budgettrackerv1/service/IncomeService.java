package com.example.budgettrackerv1.service;

import com.example.budgettrackerv1.model.Income;
import com.example.budgettrackerv1.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {

    private final IncomeRepository INCOME_REPOSITORY;

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
            System.out.printf("[%s] Could not save income.%n", e.getLocalizedMessage());
            return false;
        }
    }

    public boolean update(Income income) {
        if (!this.INCOME_REPOSITORY.existsById(income.getId())) {
            System.out.printf("Could not find income with ID %d.%n", income.getId());
            return false;
        }
        try {
            this.INCOME_REPOSITORY.save(income);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.printf("[%s] Could not update income with ID %d.%n", e.getLocalizedMessage(), income.getId());
            return false;
        }
    }

    public boolean delete(int id) {
        if (!this.INCOME_REPOSITORY.existsById(id)) {
            System.out.printf("Could not find income with ID %d.%n", id);
            return false;
        }
        try {
            this.INCOME_REPOSITORY.findById(id).ifPresent(this.INCOME_REPOSITORY::delete);
            return true;
        } catch (Exception e) {
            System.out.printf("[%s] Could not delete income with ID %d.%n", e.getLocalizedMessage(), id);
            return false;
        }
    }
}
