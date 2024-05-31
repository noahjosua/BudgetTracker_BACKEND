package com.example.budgettrackerv1.controller;

import com.example.budgettrackerv1.Constants;
import com.example.budgettrackerv1.model.Expense;
import com.example.budgettrackerv1.service.ExpenseService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = Constants.ALLOWED_ORIGIN)
public class ExpenseController {

    private final ExpenseService EXPENSE_SERVICE;
    private final Gson gson = new Gson();

    @Autowired
    public ExpenseController(ExpenseService EXPENSE_SERVICE) {
        this.EXPENSE_SERVICE = EXPENSE_SERVICE;
    }

    @GetMapping("expenses")
    public ResponseEntity<String> getAllExpenses() {
        List<Expense> expenses = EXPENSE_SERVICE.getExpenses();
        for (Expense expense : expenses) {
            System.out.println(expense);
        }
        return ResponseEntity.ok(gson.toJson(expenses));
    }

    @GetMapping("expense/{id}")
    public ResponseEntity<String> getExpenseById(@PathVariable int id) {
        Expense expense = this.EXPENSE_SERVICE.getById(id);
        return ResponseEntity.ok(gson.toJson(expense));
    }

    @PostMapping("/saveExpense")
    public ResponseEntity<String> save(@RequestBody String jsonExpense) {
        Expense expense = this.gson.fromJson(jsonExpense, Expense.class);
        System.out.println(expense);
        this.EXPENSE_SERVICE.save(expense);
        System.out.println(expense);
        String message = String.format("Expense with id %d was saved successfully", expense.getId());
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/deleteExpense/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        this.EXPENSE_SERVICE.delete(id);
        String message = String.format("Expense with id %d was deleted successfully", id);
        return ResponseEntity.ok(message);
    }
}
