package com.example.budgettrackerv1.controller;

import com.example.budgettrackerv1.Constants;
import com.example.budgettrackerv1.MockData;
import com.example.budgettrackerv1.model.Category;
import com.example.budgettrackerv1.model.Expense;
import com.example.budgettrackerv1.model.Income;
import com.example.budgettrackerv1.service.IncomeService;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = Constants.ALLOWED_ORIGIN)
public class IncomeController {

    private final IncomeService INCOME_SERVICE;
    private final Gson gson = new Gson();


    @Autowired
    public IncomeController(IncomeService INCOME_SERVICE) {
        this.INCOME_SERVICE = INCOME_SERVICE;
    }

    @GetMapping("income/categories")
    public ResponseEntity<String> getAllCategories() {
        List<Category> categories = List.of(
                Category.SALARY, Category.POCKET_MONEY, Category.ALIMENT, Category.CAPITAL_ASSETS, Category.RENTAL, Category.OTHER
        );
        return ResponseEntity.ok(gson.toJson(categories));
    }

    @PostConstruct
    public void init() {
        /* Nur zum testen - sollte wieder gelöscht werden */
        readAllIncomesFromDB();
        saveIncomesToDB();
        editIncomeInDB();
        deleteIncomeFromDB();
    }

    /* Nur zum sicherstellen, dass DB Zugriff funktioniert - sollte wieder gelöscht werden */
    public void readAllIncomesFromDB() {
        System.out.println("#### INCOME CONTROLLER - READ FROM DB ####");
        List<Income> incomes = this.INCOME_SERVICE.getIncomes();
        for (Income income : incomes) {
            System.out.println(income);
        }
    }

    public void saveIncomesToDB() {
        System.out.println("#### INCOME CONTROLLER - SAVE TO DB ####");
        Income income = this.gson.fromJson(MockData.json2, Income.class);
        System.out.println(income);
        this.INCOME_SERVICE.save(income);
        System.out.println(income);
    }

    public void editIncomeInDB() {
        System.out.println("#### INCOME CONTROLLER - EDIT TO DB ####");
        Income incomeToEdit = this.INCOME_SERVICE.getById(1);
        System.out.printf("Income to edit: %s.%n", incomeToEdit);
        incomeToEdit.setAmount(300.00);
        System.out.printf("Save updated income %s.%n", incomeToEdit);
        this.INCOME_SERVICE.save(incomeToEdit);
    }

    public void deleteIncomeFromDB() {
        System.out.println("#### INCOME CONTROLLER - DELETE FROM DB ####");
        Income income = this.INCOME_SERVICE.getById(1);
        this.INCOME_SERVICE.delete(income.getId());
    }
    /* Ende */

    @GetMapping("incomes")
    public ResponseEntity<String> getAllIncomes() {
        List<Income> incomes = INCOME_SERVICE.getIncomes();
        if (incomes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No incomes found.");
        }

        for (Income income : incomes) {
            System.out.println(income);
        }
        return ResponseEntity.ok(gson.toJson(incomes));
    }

    @GetMapping("income/{id}")
    public ResponseEntity<String> getIncomeById(@PathVariable int id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().body("Provided ID is not valid.");
        }
        Income income = this.INCOME_SERVICE.getById(id);
        if (income == null || income.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Expense with ID %d not found", id));
        }
        return ResponseEntity.ok(gson.toJson(income));
    }

    @PostMapping("/saveIncome")
    public ResponseEntity<String> save(@RequestBody String jsonIncome) {
        if(jsonIncome == null || jsonIncome.isEmpty()) {
            String message = "Income cannot be empty";
            return ResponseEntity.badRequest().body(message);
        }
        Income income = this.gson.fromJson(jsonIncome, Income.class);
        System.out.println(income);
        this.INCOME_SERVICE.save(income);
        System.out.println(income);
        String message = String.format("Income with id %d was saved successfully", income.getId());
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/deleteIncome/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        if (id <= 0) {
            String message = "Income ID must be a positive integer";
            return ResponseEntity.badRequest().body(message);
        }
        try {
            this.INCOME_SERVICE.delete(id);
        } catch (Exception e) {
            String message = String.format("Income with id %d could not be found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        String message = String.format("Income with id %d was deleted successfully", id);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/updateIncome/{id}")
    public ResponseEntity<String> update(@RequestBody String jsonIncome, @PathVariable int id) {
        if(jsonIncome == null || jsonIncome.isEmpty()) {
            String message = "Income cannot be empty";
            return ResponseEntity.badRequest().body(message);
        } else if (id <= 0) {
            String message = "Income ID must be a positive integer";
            return ResponseEntity.badRequest().body(message);
        }
        Income income = this.gson.fromJson(jsonIncome, Income.class);
        income.setId(id);
        System.out.println(income);
        try {
            this.INCOME_SERVICE.update(income);
        } catch (Exception e) {
            String message = String.format("Income with id %d could not be found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        String message = String.format("Income with id %d was updated successfully", id);
        return ResponseEntity.ok(message);
    }
}
