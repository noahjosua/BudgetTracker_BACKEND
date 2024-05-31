package com.example.budgettrackerv1.controller;

import com.example.budgettrackerv1.Constants;
import com.example.budgettrackerv1.model.Expense;
import com.example.budgettrackerv1.model.Income;
import com.example.budgettrackerv1.service.IncomeService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = Constants.ALLOWED_ORIGIN)
public class IncomeController {

    private final IncomeService INCOME_SERVICE;
    private final Gson gson = new Gson();


    @Autowired
    public IncomeController(IncomeService INCOME_SERVICE) {
        this.INCOME_SERVICE = INCOME_SERVICE;
    }

    @GetMapping("incomes")
    public ResponseEntity<String> getAllIncomes() {
        List<Income> incomes = INCOME_SERVICE.getIncomes();
        for (Income income : incomes) {
            System.out.println(income);
        }
        return ResponseEntity.ok(gson.toJson(incomes));
    }

    @GetMapping("income/{id}")
    public ResponseEntity<String> getIncomeById(@PathVariable int id) {
        Income income = this.INCOME_SERVICE.getById(id);
        return ResponseEntity.ok(gson.toJson(income));
    }

    @PostMapping("/saveIncome")
    public ResponseEntity<String> save(@RequestBody String jsonIncome) {
        Income income = this.gson.fromJson(jsonIncome, Income.class);
        System.out.println(income);
        this.INCOME_SERVICE.save(income);
        System.out.println(income);
        String message = String.format("Income with id %d was saved successfully", income.getId());
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/deleteIncome/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        this.INCOME_SERVICE.delete(id);
        String message = String.format("Income with id %d was deleted successfully", id);
        return ResponseEntity.ok(message);
    }
}
