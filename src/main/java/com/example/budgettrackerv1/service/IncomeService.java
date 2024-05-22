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

    private final IncomeRepository incomeRepository;

    @Autowired
    public IncomeService(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    public List<Income> getIncomes(){
        return this.incomeRepository.findAll();
    }

    public void save(Income income){
        this.incomeRepository.save(income);
    }

    public void delete(int id){
        this.incomeRepository.deleteById(id);
    }

    public Income getById(int id){
        return this.incomeRepository.findById(id).get();
    }

    public List<Income> getByPlannedDate(Date date){
        List<Income> incomesByPlannedDate = new ArrayList<>();
        for(Income income : this.incomeRepository.findAll()){
            if(income.getDatePlanned().equals(date)){
                incomesByPlannedDate.add(income);
            }
        }
        return incomesByPlannedDate;
    }

    public List<Income> getByCreatedDate(Date date){
        List<Income> incomesByCreatedDate = new ArrayList<>();
        for(Income income : this.incomeRepository.findAll()){
            if(income.getDateCreated().equals(date)){
                incomesByCreatedDate.add(income);
            }
        }
        return incomesByCreatedDate;
    }

}
