package com.example.budgettrackerv1.controller;

import com.example.budgettrackerv1.Constants;
import com.example.budgettrackerv1.exception.EntryNotFoundException;
import com.example.budgettrackerv1.exception.EntryNotProcessedException;
import com.example.budgettrackerv1.exception.InternalServerError;
import com.example.budgettrackerv1.helper.GetEntriesByDateHelper;
import com.example.budgettrackerv1.model.Category;
import com.example.budgettrackerv1.adapter.LocalDateTypeAdapter;
import com.example.budgettrackerv1.helper.ValidateEntryHelper;
import com.example.budgettrackerv1.model.Income;
import com.example.budgettrackerv1.service.IncomeService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;


@RestController
@CrossOrigin(origins = Constants.ALLOWED_ORIGIN)
@RequestMapping(Constants.REQUEST_MAPPING_INCOME)
public class IncomeController {

    private final IncomeService INCOME_SERVICE;

    private final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .create();

    private static final Logger LOGGER = LogManager.getLogger(IncomeController.class);

    @Autowired
    public IncomeController(IncomeService INCOME_SERVICE) {
        this.INCOME_SERVICE = INCOME_SERVICE;
    }

    @GetMapping("/categories")
    public ResponseEntity<String> getAllCategories() {
        return ResponseEntity.ok(GSON.toJson(List.of(
                Category.SALARY, Category.POCKET_MONEY, Category.ALIMENT, Category.CAPITAL_ASSETS, Category.RENTAL, Category.OTHER
        )));
    }

    @GetMapping("/byDate/{date}")
    public ResponseEntity<String> getIncomesByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        Optional<LocalDate> firstDay = GetEntriesByDateHelper.getDate(date, Constants.FIRST_DAY_KEY);
        Optional<LocalDate> lastDay = GetEntriesByDateHelper.getDate(date, Constants.LAST_DAY_KEY);
        String messageSuccess = GetEntriesByDateHelper.getSuccessMessageForByDateRequest(date, Constants.TYPE_INCOMES);
        String messageError = GetEntriesByDateHelper.getErrorMessageForByDateRequest(date, Constants.TYPE_INCOMES);

        if (firstDay.isPresent() && lastDay.isPresent()) {
            Optional<List<Income>> optionalIncomes = this.INCOME_SERVICE.getByDate(firstDay.get(), lastDay.get());
            if (optionalIncomes.isPresent()) {
                Map<String, Object> response = ValidateEntryHelper.buildResponseBody(messageSuccess, optionalIncomes.get());
                return ResponseEntity.ok(this.GSON.toJson(response));
            }
            LOGGER.info("No Incomes were found for the specified date. Message: {}", messageError);
            throw new EntryNotFoundException(messageError);
        }
        LOGGER.info("No Incomes were found for the specified date. Message: {}",messageError);
        throw new EntryNotFoundException(messageError);
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody String jsonIncome) {
        Income income = this.GSON.fromJson(jsonIncome, Income.class);
        try {
            ValidateEntryHelper.isValid(income);
            boolean isSaved = this.INCOME_SERVICE.save(income);
            if (isSaved) {
                String message = String.format("Income with id %d was saved successfully.", income.getId());
                Map<String, Object> response = ValidateEntryHelper.buildResponseBody(message, income);
                LOGGER.info(message);
                return ResponseEntity.ok(this.GSON.toJson(response));
            }
        } catch (IllegalArgumentException | EntryNotProcessedException e) {
            LOGGER.error("Could not save income. Error: {}.", e.getLocalizedMessage(), e);
            return ResponseEntity.badRequest().body(String.format("Could not save income. Error: %s.", e.getMessage()));
        }
        throw new InternalServerError("An unexpected Error occurred. Could not save income.");
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody String jsonIncome) {
        Income income = this.GSON.fromJson(jsonIncome, Income.class);
        try {
            ValidateEntryHelper.isValid(income);
            boolean isUpdated = this.INCOME_SERVICE.update(income);
            if (isUpdated) {
                String message = String.format("Income with id %d was updated successfully.", income.getId());
                Map<String, Object> response = ValidateEntryHelper.buildResponseBody(message, income);
                LOGGER.info(message);
                return ResponseEntity.ok(this.GSON.toJson(response));
            }
        } catch (IllegalArgumentException | EntryNotProcessedException e) {
            LOGGER.error("Could not update income. Error: {}.", e.getLocalizedMessage(), e);
            return ResponseEntity.badRequest().body(String.format("Could not update income. Error: %s.", e.getMessage()));
        } catch (EntryNotFoundException e) {
            LOGGER.error("Could not find income to update. Error: {}.", e.getLocalizedMessage(), e);
            return ResponseEntity.notFound().build();
        }
        throw new InternalServerError("An unexpected Error occurred. Could not update income.");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        boolean isDeleted = this.INCOME_SERVICE.delete(id);
        if (isDeleted) {
            String message = String.format("Income with id %d was deleted successfully.", id);
            LOGGER.info(message);
            return ResponseEntity.ok(message);
        }
        String message = String.format("Income with id %d could not be deleted.", id);
        LOGGER.info(message);
        return ResponseEntity.badRequest().body(message);
    }
}
