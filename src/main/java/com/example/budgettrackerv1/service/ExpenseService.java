package com.example.budgettrackerv1.service;

import com.example.budgettrackerv1.model.Expense;
import com.example.budgettrackerv1.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> getExpenses(){
        return this.expenseRepository.findAll();
    }

    public void save(Expense expense){
        this.expenseRepository.save(expense);
    }

    public void delete(int id){
        this.expenseRepository.deleteById(id);
    }

    public Expense getById(int id){
        return this.expenseRepository.findById(id).get();
    }

    public List<Expense> getByPlannedDate(Date date){
        List<Expense> expensesByPlannedDate = new ArrayList<>();
        for(Expense expense : this.expenseRepository.findAll()){
            if(expense.getDatePlanned().equals(date)){
                expensesByPlannedDate.add(expense);
            }
        }
        return expensesByPlannedDate;
    }

    public List<Expense> getByCreatedDate(Date date){
        List<Expense> expensesByCreatedDate = new ArrayList<>();
        for(Expense expense : this.expenseRepository.findAll()){
            if(expense.getDateCreated().equals(date)){
                expensesByCreatedDate.add(expense);
            }
        }
        return expensesByCreatedDate;
    }
}
