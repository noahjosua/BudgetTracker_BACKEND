package com.team7.budgettracker.controller;

import com.team7.budgettracker.Constants;
import com.team7.budgettracker.exception.EntryNotFoundException;
import com.team7.budgettracker.exception.EntryNotProcessedException;
import com.team7.budgettracker.exception.InternalServerError;
import com.team7.budgettracker.helper.GetEntriesByDateHelper;
import com.team7.budgettracker.model.Category;
import com.team7.budgettracker.adapter.LocalDateTypeAdapter;
import com.team7.budgettracker.helper.ValidateEntryHelper;
import com.team7.budgettracker.model.Income;
import com.team7.budgettracker.service.IncomeService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Income", description = "Controller for managing incomes")
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

    @Operation(
            description = "Get all available income categories",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping("/categories")
    public ResponseEntity<String> getAllCategories() {
        return ResponseEntity.ok(GSON.toJson(List.of(
                Category.SALARY, Category.POCKET_MONEY, Category.ALIMENT, Category.CAPITAL_ASSETS, Category.RENTAL, Category.OTHER
        )));
    }

    @Operation(
            description = "Extracts the month from the specified date (ISO Format: yyyy-MM-dd) and returns all incomes for that month",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404"
                    )
            }

    )
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
        LOGGER.info("No Incomes were found for the specified date. Message: {}", messageError);
        throw new EntryNotFoundException(messageError);
    }

    @Operation(
            description = "Saves an income in JSON format. Returns the income if successful. " +
                    "Example: {\"dateCreated\":\"2024-06-18\",\"datePlanned\":\"2024-06-26\",\"category\":\"POCKET_MONEY\",\"description\":\"Test\",\"amount\":3}",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Internal Server Error",
                            responseCode = "500"
                    )
            }
    )
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

    @Operation(
            description = "Updates an income in JSON format if the specified income exists. Returns the income if successful. " +
                    "Example: {\"id\":\"502\",\"dateCreated\":\"2024-06-18\",\"datePlanned\":\"2024-06-26\",\"category\":\"POCKET_MONEY\",\"description\":\"Test\",\"amount\":3}",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Not found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Internal Server Error",
                            responseCode = "500"
                    )
            }
    )
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

    @Operation(
            description = "Deletes an income with a given ID",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400"
                    )
            }
    )
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
