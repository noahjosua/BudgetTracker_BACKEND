package com.example.budgettrackerv1.service;

import com.example.budgettrackerv1.model.Income;
import com.example.budgettrackerv1.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class IncomeService {

    private final IncomeRepository INCOME_REPOSITORY;

    @Autowired
    public IncomeService(IncomeRepository incomeRepository) {
        this.INCOME_REPOSITORY = incomeRepository;
    }

    public List<Income> getIncomes(){
        return this.INCOME_REPOSITORY.findAll();
    }

    public void save(Income income){
        this.INCOME_REPOSITORY.save(income);
    }

    public void delete(int id){
        if(!this.INCOME_REPOSITORY.existsById(id)){
            throw new RuntimeException("Income not found");
        }
        this.INCOME_REPOSITORY.findById(id).ifPresent(this.INCOME_REPOSITORY::delete);
    }

    public Income getById(int id){
        return this.INCOME_REPOSITORY.findById(id).orElseThrow(() -> new RuntimeException("Income not found"));
    }

    public List<Income> getByPlannedDate(Date date){
        List<Income> incomesByPlannedDate = new ArrayList<>();
        for(Income income : this.INCOME_REPOSITORY.findAll()){
            if(income.getDatePlanned().equals(date)){
                incomesByPlannedDate.add(income);
            }
        }
        return incomesByPlannedDate;
    }

    public List<Income> getByCreatedDate(Date date){
        List<Income> incomesByCreatedDate = new ArrayList<>();
        for(Income income : this.INCOME_REPOSITORY.findAll()){
            if(income.getDateCreated().equals(date)){
                incomesByCreatedDate.add(income);
            }
        }
        return incomesByCreatedDate;
    }

}
