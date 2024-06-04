package com.example.budgettrackerv1;

import com.example.budgettrackerv1.model.Expense;
import com.example.budgettrackerv1.service.ExpenseService;
import com.google.gson.Gson;

import java.util.List;
import java.util.Optional;

// TODO löschen wenn nicht mehr gebraucht
public class TestDbConnectionForExpense {

    public static void readAllExpensesFromDB(ExpenseService expenseService) {
        Optional<List<Expense>> expenses = expenseService.getExpenses();
        if (expenses.isPresent()) {
            for (Expense expense : expenses.get()) {
                System.out.println(expense);
            }
        }
    }

    public static void saveExpenseToDB(Gson gson, ExpenseService expenseService) {
        Expense expense = gson.fromJson(MockData.json1, Expense.class);
        System.out.println(expense);
        expenseService.save(expense);
        System.out.println(expense);
    }

    public static void editExpenseInDB(ExpenseService expenseService) {
        Optional<Expense> expenseToEdit = expenseService.getById(1);
        if (expenseToEdit.isPresent()) {
            System.out.printf("Expense to edit: %s.%n", expenseToEdit.get());
            expenseToEdit.get().setAmount(300.00);
            System.out.printf("Save updated expense %s.%n", expenseToEdit.get());
            expenseService.save(expenseToEdit.get());
        }
    }

    public static void deleteExpenseFromDB(ExpenseService expenseService) {
        Optional<Expense> expense = expenseService.getById(1);
        expense.ifPresent(value -> expenseService.delete(value.getId()));
    }
}
