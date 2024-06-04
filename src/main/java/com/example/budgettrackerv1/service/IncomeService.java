package com.example.budgettrackerv1.service;

import com.example.budgettrackerv1.model.Income;
import com.example.budgettrackerv1.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeService {

    private final IncomeRepository INCOME_REPOSITORY;

    @Autowired
    public IncomeService(IncomeRepository incomeRepository) {
        this.INCOME_REPOSITORY = incomeRepository;
    }

    // TODO -- an ExpenseService anpassen, wenn ihr einverstanden seid?
    public List<Income> getIncomes() {
        return this.INCOME_REPOSITORY.findAll();
    }

    // TODO -- an ExpenseService anpassen, wenn ihr einverstanden seid?
    public Income getById(int id) {
        return this.INCOME_REPOSITORY.findById(id).orElseThrow(() -> new RuntimeException("Income not found"));
    }

    // TODO -- an ExpenseService anpassen, wenn ihr einverstanden seid?
    public void save(Income income) {
        this.INCOME_REPOSITORY.save(income);
    }

    // TODO Errorhandling
    public void delete(int id) {
        if (!this.INCOME_REPOSITORY.existsById(id)) {
            throw new RuntimeException("Income not found");
        }
        this.INCOME_REPOSITORY.findById(id).ifPresent(this.INCOME_REPOSITORY::delete);
    }

    // TODO Errorhandling
    public void update(Income income) {
        if (!this.INCOME_REPOSITORY.existsById(income.getId())) {
            throw new RuntimeException("Income not found");
        }
        this.INCOME_REPOSITORY.save(income);
    }
}
