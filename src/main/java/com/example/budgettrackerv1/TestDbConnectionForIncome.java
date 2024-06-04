package com.example.budgettrackerv1;

import com.example.budgettrackerv1.model.Income;
import com.example.budgettrackerv1.service.IncomeService;
import com.google.gson.Gson;

import java.util.List;

// TODO löschen wenn nicht mehr gebraucht
public class TestDbConnectionForIncome {
    public static void readAllIncomesFromDB(IncomeService incomeService) {
        List<Income> incomes = incomeService.getIncomes();
        for (Income income : incomes) {
            System.out.println(income);
        }
    }

    public static void saveIncomesToDB(Gson gson, IncomeService incomeService) {
        Income income = gson.fromJson(MockData.json2, Income.class);
        System.out.println(income);
        incomeService.save(income);
        System.out.println(income);
    }

    public static void editIncomeInDB(IncomeService incomeService) {
        Income incomeToEdit = incomeService.getById(1);
        System.out.printf("Income to edit: %s.%n", incomeToEdit);
        incomeToEdit.setAmount(300.00);
        System.out.printf("Save updated income %s.%n", incomeToEdit);
        incomeService.save(incomeToEdit);
    }

    public static void deleteIncomeFromDB(IncomeService incomeService) {
        Income income = incomeService.getById(1);
        incomeService.delete(income.getId());
    }
}
