package com.example.budgettrackerv1.controller;

import com.example.budgettrackerv1.Constants;
import com.example.budgettrackerv1.MockData;
import com.example.budgettrackerv1.adapter.LocalDateTypeAdapter;
import com.example.budgettrackerv1.helper.Helper;
import com.example.budgettrackerv1.model.Category;
import com.example.budgettrackerv1.model.Expense;
import com.example.budgettrackerv1.service.ExpenseService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.*;

import static com.example.budgettrackerv1.TestDbConnectionForExpense.*;


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

    @PostConstruct
    public void init() {
        /* TODO Nur zum testen - sollte wieder gelöscht werden */
        System.out.println("#### EXPENSE CONTROLLER - READ FROM DB ####");
        readAllExpensesFromDB(this.EXPENSE_SERVICE);
        System.out.println("#### EXPENSE CONTROLLER - SAVE TO DB ####");
        saveExpenseToDB(this.GSON, this.EXPENSE_SERVICE);
        System.out.println("#### EXPENSE CONTROLLER - EDIT TO DB ####");
        editExpenseInDB(this.EXPENSE_SERVICE);
        // System.out.println("#### EXPENSE CONTROLLER - DELETE FROM DB ####");
        // deleteExpenseFromDB(this.EXPENSE_SERVICE);
    }

    @GetMapping("/categories")
    public ResponseEntity<String> getAllCategories() {
        try {
            return ResponseEntity.ok(this.GSON.toJson(List.of(
                    Category.GROCERIES, Category.DRUGSTORE, Category.FREE_TIME, Category.RENT, Category.INSURANCE, Category.SUBSCRIPTIONS, Category.EDUCATION, Category.OTHER
            )));
        } catch (Exception e) {
            System.out.println("Could not serialize categories.");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // TODO ist das der richtige code?
    }

    @GetMapping("/byDate/{date}")
    public ResponseEntity<String> getExpensesByDate(@PathVariable Date date) {
        Optional<LocalDate> firstDay = Helper.getDate(date, Constants.FIRST_DAY_KEY);
        Optional<LocalDate> lastDay = Helper.getDate(date, Constants.LAST_DAY_KEY);
        String messageSuccess = Helper.getSuccessMessageForByIdRequest(date, Constants.TYPE_EXPENSES);
        String messageError = Helper.getErrorMessageForByIdRequest(date, Constants.TYPE_EXPENSES);

        if (firstDay.isPresent() && lastDay.isPresent()) {
            Optional<List<Expense>> optionalExpenses = this.EXPENSE_SERVICE.getByDate(firstDay.get(), lastDay.get());
            if (optionalExpenses.isPresent()) {
                List<Expense> expenses = optionalExpenses.get();
                Map<String, Object> response = Map.of(
                        Constants.RESPONSE_MESSAGE_KEY, messageSuccess,
                        Constants.RESPONSE_ENTRY_KEY, expenses
                );
                try {
                    return ResponseEntity.ok(this.GSON.toJson(response));
                } catch (Exception e) {
                    System.out.println("Could not serialize response.");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageError);
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<String> getExpenseById(@PathVariable int id) {
        Optional<Expense> optionalExpense = this.EXPENSE_SERVICE.getById(id);
        if (optionalExpense.isPresent()) {
            Expense expense = optionalExpense.get();
            String message = String.format("Expense with id %d was retrieved from database.", expense.getId());
            Map<String, Object> response = Map.of(
                    Constants.RESPONSE_MESSAGE_KEY, message,
                    Constants.RESPONSE_ENTRY_KEY, expense
            );
            try {
                return ResponseEntity.ok(this.GSON.toJson(response));
            } catch (Exception e) {
                System.out.println("Could not serialize response.");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense could not be retrieved from database.");
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody String jsonExpense) {
        try {
            Expense expense = this.GSON.fromJson(jsonExpense, Expense.class);
            boolean isSaved = this.EXPENSE_SERVICE.save(expense);
            if (isSaved) {
                String message = String.format("Expense with id %d was saved successfully.", expense.getId());
                Map<String, Object> response = Map.of(
                        Constants.RESPONSE_MESSAGE_KEY, message,
                        Constants.RESPONSE_ENTRY_KEY, expense
                );
                try {
                    return ResponseEntity.ok(this.GSON.toJson(response));
                } catch (Exception e) {
                    System.out.println("Could not serialize response.");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error while serializing response.");
                }
            } else {
                String message = String.format("Expense with id %d could not be saved.", expense.getId());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            }
        } catch (JsonSyntaxException e) {
            return ResponseEntity.unprocessableEntity().body("Expense could not be deserialized.");
        } catch (Exception e) {
            String message = "An unexpected error occurred while saving expense.";
            System.out.printf("Error: %s%n", e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@RequestBody String jsonExpense, @PathVariable int id) {
        try {
            Expense expense = this.GSON.fromJson(jsonExpense, Expense.class);
            expense.setId(id);
            boolean isUpdated = this.EXPENSE_SERVICE.update(expense);

            if (isUpdated) {
                String message = String.format("Expense with id %d was updated successfully.", id);
                Map<String, Object> response = Map.of(
                        Constants.RESPONSE_MESSAGE_KEY, message,
                        Constants.RESPONSE_ENTRY_KEY, expense
                );
                try {
                    return ResponseEntity.ok(this.GSON.toJson(response));
                } catch (Exception e) {
                    System.out.println("Could not serialize response.");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error while serializing response.");
                }
            } else {
                String message = String.format("Expense with id %d could not be updated successfully.", id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            }
        } catch (JsonSyntaxException e) {
            return ResponseEntity.unprocessableEntity().body("Expense could not be deserialized.");
        } catch (Exception e){
            String message = String.format("An unexpected error occurred while updating expense with id %d.", id);
            System.out.printf("Error: %s%n", e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        try {
            boolean isDeleted = this.EXPENSE_SERVICE.delete(id);
            if(isDeleted) {
                String message = String.format("Expense with id %d was deleted successfully.", id);
                try {
                    return ResponseEntity.ok(message);
                } catch (Exception e) {
                    System.out.println("Could not serialize response.");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error while serializing response.");
                }
            } else {
                String message = String.format("Expense with id %d could not be deleted.", id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            }
        } catch (Exception e) {
            String message = String.format("An unexpected error occurred while updating expense with id %d.", id);
            System.out.printf("Error: %s%n", e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }
}
