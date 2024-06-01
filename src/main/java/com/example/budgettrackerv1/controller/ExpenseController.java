package com.example.budgettrackerv1.controller;

import com.example.budgettrackerv1.Constants;
import com.example.budgettrackerv1.model.Expense;
import com.example.budgettrackerv1.service.ExpenseService;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = Constants.ALLOWED_ORIGIN)
public class ExpenseController {

    private final ExpenseService EXPENSE_SERVICE;
    private final Gson gson = new Gson();

    @Autowired
    public ExpenseController(ExpenseService EXPENSE_SERVICE) {
        this.EXPENSE_SERVICE = EXPENSE_SERVICE;
    }

    @GetMapping("expense/categories")
    public ResponseEntity<String> getAllCategories() {
        List<Category> categories = List.of(
                Category.GROCERIES, Category.DRUGSTORE, Category.FREE_TIME, Category.RENT, Category.INSURANCE, Category.SUBSCRIPTIONS, Category.EDUCATION, Category.OTHER
        );
        return ResponseEntity.ok(gson.toJson(categories));
    }

    @PostConstruct
    public void init() {
        /* Nur zum testen - sollte wieder gelöscht werden */
        readAllExpensesFromDB();
        saveExpenseToDB();
        editExpenseInDB();
        deleteExpenseFromDB();
    }

    /* Nur zum sicherstellen, dass DB Zugriff funktioniert - sollte wieder gelöscht werden */
    public void readAllExpensesFromDB() {
        System.out.println("#### EXPENSE CONTROLLER - READ FROM DB ####");
        List<Expense> expenses = this.EXPENSE_SERVICE.getExpenses();
        for (Expense expense : expenses) {
            System.out.println(expense);
        }
    }

    public void saveExpenseToDB() {
        System.out.println("#### EXPENSE CONTROLLER - SAVE TO DB ####");
        Expense expense = this.gson.fromJson(MockData.json1, Expense.class);
        System.out.println(expense);
        this.EXPENSE_SERVICE.save(expense);
        System.out.println(expense);
    }

    public void editExpenseInDB() {
        System.out.println("#### EXPENSE CONTROLLER - EDIT TO DB ####");
        Expense expenseToEdit = this.EXPENSE_SERVICE.getById(1);
        System.out.printf("Expense to edit: %s.%n", expenseToEdit);
        expenseToEdit.setAmount(300.00);
        System.out.printf("Save updated expense %s.%n", expenseToEdit);
        this.EXPENSE_SERVICE.save(expenseToEdit);
    }

    public void deleteExpenseFromDB() {
        System.out.println("#### EXPENSE CONTROLLER - DELETE FROM DB ####");
        Expense expense = this.EXPENSE_SERVICE.getById(1);
        this.EXPENSE_SERVICE.delete(expense.getId());
    }
    /* Ende */

    @GetMapping("expenses")
    public ResponseEntity<String> getAllExpenses() {
        List<Expense> expenses = this.EXPENSE_SERVICE.getExpenses();
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
