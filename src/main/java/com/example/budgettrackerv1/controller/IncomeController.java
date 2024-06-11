package com.example.budgettrackerv1.controller;

import com.example.budgettrackerv1.Constants;
import com.example.budgettrackerv1.adapter.LocalDateTypeAdapter;
import com.example.budgettrackerv1.helper.Helper;
import com.example.budgettrackerv1.model.Category;
import com.example.budgettrackerv1.model.Income;
import com.example.budgettrackerv1.service.IncomeService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

import static com.example.budgettrackerv1.TestDbConnectionForIncome.*;

@RestController
@CrossOrigin(origins = Constants.ALLOWED_ORIGIN)
@RequestMapping(Constants.REQUEST_MAPPING_INCOME)
public class IncomeController {

    private final IncomeService INCOME_SERVICE;
    private final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .create();

    @Autowired
    public IncomeController(IncomeService INCOME_SERVICE) {
        this.INCOME_SERVICE = INCOME_SERVICE;
    }

    @PostConstruct
    public void init() {
        /* Nur zum testen - sollte wieder gelöscht werden */
        System.out.println("#### INCOME CONTROLLER - READ FROM DB ####");
        readAllIncomesFromDB(this.INCOME_SERVICE);
        System.out.println("#### INCOME CONTROLLER - SAVE TO DB ####");
        saveIncomesToDB(this.GSON, this.INCOME_SERVICE);
        System.out.println("#### INCOME CONTROLLER - EDIT TO DB ####");
        editIncomeInDB(this.INCOME_SERVICE);
        //System.out.println("#### INCOME CONTROLLER - DELETE FROM DB ####");
        //deleteIncomeFromDB(this.INCOME_SERVICE);
    }

    @GetMapping("/categories")
    public ResponseEntity<String> getAllCategories() {
        try {
            return ResponseEntity.ok(GSON.toJson(List.of(
                    Category.SALARY, Category.POCKET_MONEY, Category.ALIMENT, Category.CAPITAL_ASSETS, Category.RENTAL, Category.OTHER
            )));
        } catch (Exception e) {
            System.out.println("Could not serialize categories.");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // TODO ist das der richtige code?
    }


    @GetMapping("/byDate/{date}")
    public ResponseEntity<String> getIncomesByDate(@PathVariable Date date) {
        Optional<LocalDate> firstDay = Helper.getDate(date, Constants.FIRST_DAY_KEY);
        Optional<LocalDate> lastDay = Helper.getDate(date, Constants.LAST_DAY_KEY);
        String messageSuccess = Helper.getSuccessMessageForByIdRequest(date, Constants.TYPE_INCOMES);
        String messageError = Helper.getErrorMessageForByIdRequest(date, Constants.TYPE_INCOMES);

        if (firstDay.isPresent() && lastDay.isPresent()) {
            Optional<List<Income>> optionalIncomes = this.INCOME_SERVICE.getByDate(firstDay.get(), lastDay.get());
            if (optionalIncomes.isPresent()) {
                List<Income> incomes = optionalIncomes.get();
                Map<String, Object> response = Map.of(
                        Constants.RESPONSE_MESSAGE_KEY, messageSuccess,
                        Constants.RESPONSE_ENTRY_KEY, incomes
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

    /*
    @GetMapping("")
    public ResponseEntity<String> getAllIncomes() {
        List<Income> incomes = INCOME_SERVICE.getIncomes();
        if (incomes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No incomes found.");
        }

        for (Income income : incomes) {
            System.out.println(income);
        }
        return ResponseEntity.ok(GSON.toJson(incomes));
    }
     */

    // TODO Errorhandling --> vielleicht so ähnlich wie in der Expense byId?
    @GetMapping("/byId/{id}")
    public ResponseEntity<String> getIncomeById(@PathVariable int id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().body("Provided ID is not valid.");
        }
        Income income = this.INCOME_SERVICE.getById(id);
        if (income == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Expense with ID %d not found", id));
        }
        return ResponseEntity.ok(GSON.toJson(income));
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody String jsonIncome) {
        try {
            Income income = this.GSON.fromJson(jsonIncome, Income.class);
            boolean isSaved = this.INCOME_SERVICE.save(income);
            if (isSaved) {
                String message = String.format("Income with id %d was saved successfully.", income.getId());
                Map<String, Object> response = Map.of(
                        Constants.RESPONSE_MESSAGE_KEY, message,
                        Constants.RESPONSE_ENTRY_KEY, income
                );
                try {
                    return ResponseEntity.ok(this.GSON.toJson(response));
                } catch (Exception e) {
                    System.out.println("Could not serialize response.");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error while serializing response.");
                }
            } else {
                String message = String.format("Income with id %d could not be saved.", income.getId());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            }
        } catch (JsonSyntaxException e) {
            return ResponseEntity.unprocessableEntity().body("Income could not be deserialized.");
        } catch (Exception e) {
            String message = "An unexpected error occurred while saving income.";
            System.out.printf("Error: %s%n", e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@RequestBody String jsonIncome, @PathVariable int id) {
        try {
            Income income = this.GSON.fromJson(jsonIncome, Income.class);
            income.setId(id);
            boolean isUpdated = this.INCOME_SERVICE.update(income);

            if (isUpdated) {
                String message = String.format("Income with id %d was updated successfully.", id);
                Map<String, Object> response = Map.of(
                        Constants.RESPONSE_MESSAGE_KEY, message,
                        Constants.RESPONSE_ENTRY_KEY, income
                );
                try {
                    return ResponseEntity.ok(this.GSON.toJson(response));
                } catch (Exception e) {
                    System.out.println("Could not serialize response.");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error while serializing response.");
                }
            } else {
                String message = String.format("Income with id %d could not be updated successfully.", id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            }
        } catch (JsonSyntaxException e) {
            return ResponseEntity.unprocessableEntity().body("Income could not be deserialized.");
        } catch (Exception e) {
            String message = String.format("An unexpected error occurred while updating income with id %d.", id);
            System.out.printf("Error: %s%n", e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    // TODO Errorhandling --> vielleicht so ähnlich wie in der Expense save?
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        try {
            boolean isDeleted = this.INCOME_SERVICE.delete(id);

            if (isDeleted) {
                String message = String.format("Income with id %d was deleted successfully.", id);
                try {
                    return ResponseEntity.ok(message);
                } catch (Exception e) {
                    System.out.println("Could not serialize response.");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error while serializing response.");
                }
            } else {
                String message = String.format("Income with id %d could not be deleted.", id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            }
        } catch (Exception e) {
            String message = String.format("An unexpected error occurred while updating income with id %d.", id);
            System.out.printf("Error: %s%n", e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }
}
