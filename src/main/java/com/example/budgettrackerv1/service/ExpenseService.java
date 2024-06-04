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

    private final ExpenseRepository EXPENSE_REPOSITORY;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.EXPENSE_REPOSITORY = expenseRepository;
    }

    public List<Expense> getExpenses(){
        return this.EXPENSE_REPOSITORY.findAll();
    }

    public void save(Expense expense){
        this.EXPENSE_REPOSITORY.save(expense);
    }

    public void delete(int id){
        if(!this.EXPENSE_REPOSITORY.existsById(id)){
            throw new RuntimeException("Expense not found");
        }
        this.EXPENSE_REPOSITORY.findById(id).ifPresent(this.EXPENSE_REPOSITORY::delete);
    }

    public Expense getById(int id){
        return this.EXPENSE_REPOSITORY.findById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
    }

    public List<Expense> getByPlannedDate(Date date){
        List<Expense> expensesByPlannedDate = new ArrayList<>();
        for(Expense expense : this.EXPENSE_REPOSITORY.findAll()){
            if(expense.getDatePlanned().equals(date)){
                expensesByPlannedDate.add(expense);
            }
        }
        return expensesByPlannedDate;
    }

    public List<Expense> getByCreatedDate(Date date){
        List<Expense> expensesByCreatedDate = new ArrayList<>();
        for(Expense expense : this.EXPENSE_REPOSITORY.findAll()){
            if(expense.getDateCreated().equals(date)){
                expensesByCreatedDate.add(expense);
            }
        }
        return expensesByCreatedDate;
    }

    public void update(Expense expense){
        if(!this.EXPENSE_REPOSITORY.existsById(expense.getId())){
            throw new RuntimeException("Expense not found");
        }
        this.EXPENSE_REPOSITORY.save(expense);
    }

}
