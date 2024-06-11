package com.example.budgettrackerv1.controller;

import com.example.budgettrackerv1.Constants;
import com.example.budgettrackerv1.adapter.LocalDateTypeAdapter;
import com.example.budgettrackerv1.exception.EntryNotProcessedException;
import com.example.budgettrackerv1.exception.EntryNotFoundException;
import com.example.budgettrackerv1.helper.GetEntriesByDateHelper;
import com.example.budgettrackerv1.helper.ValidateEntryHelper;
import com.example.budgettrackerv1.model.Category;
import com.example.budgettrackerv1.model.Expense;
import com.example.budgettrackerv1.service.ExpenseService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;


@RestController
@CrossOrigin(origins = Constants.ALLOWED_ORIGIN)
@RequestMapping(Constants.REQUEST_MAPPING_EXPENSE)
public class ExpenseController {

    private final ExpenseService EXPENSE_SERVICE;
    private final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .create();

    @Autowired
    public ExpenseController(ExpenseService EXPENSE_SERVICE) {
        this.EXPENSE_SERVICE = EXPENSE_SERVICE;
    }

    @GetMapping("/categories")
    public ResponseEntity<String> getAllCategories() {
        return ResponseEntity.ok(this.GSON.toJson(List.of(
                Category.GROCERIES, Category.DRUGSTORE, Category.FREE_TIME, Category.RENT, Category.INSURANCE, Category.SUBSCRIPTIONS, Category.EDUCATION, Category.OTHER
        )));
    }

    @GetMapping("/byDate/{date}")
    public ResponseEntity<String> getExpensesByDate(@PathVariable Date date) {
        Optional<LocalDate> firstDay = GetEntriesByDateHelper.getDate(date, Constants.FIRST_DAY_KEY);
        Optional<LocalDate> lastDay = GetEntriesByDateHelper.getDate(date, Constants.LAST_DAY_KEY);
        String messageSuccess = GetEntriesByDateHelper.getSuccessMessageForByDateRequest(date, Constants.TYPE_EXPENSES);
        String messageError = GetEntriesByDateHelper.getErrorMessageForByDateRequest(date, Constants.TYPE_EXPENSES);

        if (firstDay.isPresent() && lastDay.isPresent()) {
            Optional<List<Expense>> optionalExpenses = this.EXPENSE_SERVICE.getByDate(firstDay.get(), lastDay.get());
            if (optionalExpenses.isPresent()) {
                Map<String, Object> response = ValidateEntryHelper.buildResponseBody(messageSuccess, optionalExpenses.get());
                return ResponseEntity.ok(this.GSON.toJson(response));
            }
            throw new EntryNotFoundException(messageError);
        }
        throw new EntryNotFoundException(messageError);
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody String jsonExpense) {
        Expense expense = this.GSON.fromJson(jsonExpense, Expense.class);
        try {
            ValidateEntryHelper.isValidEntry(expense);
            boolean isSaved = this.EXPENSE_SERVICE.save(expense);
            if (isSaved) {
                String message = String.format("Expense with id %d was saved successfully.", expense.getId());
                Map<String, Object> response = ValidateEntryHelper.buildResponseBody(message, expense);
                return ResponseEntity.ok(this.GSON.toJson(response));
            }
        } catch (IllegalArgumentException e) {
            System.out.printf("[%s] Could not save expense.%n", e.getLocalizedMessage());
        }
        throw new EntryNotProcessedException("Expense could not be saved.%n");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@RequestBody String jsonExpense, @PathVariable int id) {
        Expense expense = this.GSON.fromJson(jsonExpense, Expense.class);
        try {
            ValidateEntryHelper.isValidEntry(expense);
            boolean isUpdated = this.EXPENSE_SERVICE.update(expense);
            if (isUpdated) {
                String message = String.format("Expense with id %d was updated successfully.", id);
                Map<String, Object> response = ValidateEntryHelper.buildResponseBody(message, expense);
                return ResponseEntity.ok(this.GSON.toJson(response));
            }
        } catch (IllegalArgumentException e) {
            System.out.printf("[%s] Could not update expense.%n", e.getLocalizedMessage());
        }
        String message = String.format("Expense with id %d could not be updated successfully.", id);
        throw new EntryNotProcessedException(message);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        boolean isDeleted = this.EXPENSE_SERVICE.delete(id);
        if (isDeleted) {
            String message = String.format("Expense with id %d was deleted successfully.", id);
            return ResponseEntity.ok(message);
        }
        String message = String.format("Expense with id %d could not be deleted.", id);
        throw new EntryNotProcessedException(message);
    }
}
