package com.example.budgettrackerv1.controller;

import com.example.budgettrackerv1.Constants;
import com.example.budgettrackerv1.adapter.LocalDateTypeAdapter;
import com.example.budgettrackerv1.exception.EntryNotProcessedException;
import com.example.budgettrackerv1.exception.EntryNotFoundException;
import com.example.budgettrackerv1.exception.InternalServerError;
import com.example.budgettrackerv1.helper.GetEntriesByDateHelper;
import com.example.budgettrackerv1.helper.ValidateEntryHelper;
import com.example.budgettrackerv1.model.Category;
import com.example.budgettrackerv1.model.Expense;
import com.example.budgettrackerv1.service.ExpenseService;
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
@RequestMapping(Constants.REQUEST_MAPPING_EXPENSE)
@Tag(name = "Expense", description = "Controller for managing expenses")
public class ExpenseController {

    private final ExpenseService EXPENSE_SERVICE;

    private final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .create();

    private static final Logger LOGGER = LogManager.getLogger(ExpenseController.class);

    @Autowired
    public ExpenseController(ExpenseService EXPENSE_SERVICE) {
        this.EXPENSE_SERVICE = EXPENSE_SERVICE;
    }

    @Operation(
            description = "Get all available expense categories",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping("/categories")
    public ResponseEntity<String> getAllCategories() {
        return ResponseEntity.ok(this.GSON.toJson(List.of(
                Category.GROCERIES, Category.DRUGSTORE, Category.FREE_TIME, Category.RENT, Category.INSURANCE, Category.SUBSCRIPTIONS, Category.EDUCATION, Category.OTHER
        )));
    }

    @Operation(
            description = "Extracts the month from the specified date (ISO Format: yyyy-MM-dd) and returns all expenses for that month",
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
    public ResponseEntity<String> getExpensesByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
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
            LOGGER.info("No Expenses were found for the specified date. Message: {}", messageError);
            throw new EntryNotFoundException(messageError);
        }
        LOGGER.info("Could not get dates to query the database. Message: {}", messageError);
        throw new EntryNotFoundException(messageError);
    }

    @Operation(
            description = "Saves an expense in JSON format. Returns the expense if successful. " +
                    "Example: {\"dateCreated\":\"2024-06-18\",\"datePlanned\":\"2024-06-26\",\"category\":\"FREE_TIME\",\"description\":\"Test\",\"amount\":3}",
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
    public ResponseEntity<String> save(@RequestBody String jsonExpense) {
        Expense expense = this.GSON.fromJson(jsonExpense, Expense.class);
        try {
            ValidateEntryHelper.isValid(expense);
            boolean isSaved = this.EXPENSE_SERVICE.save(expense);
            if (isSaved) {
                String message = String.format("Expense with id %d was saved successfully.", expense.getId());
                Map<String, Object> response = ValidateEntryHelper.buildResponseBody(message, expense);
                LOGGER.info(message);
                return ResponseEntity.ok(this.GSON.toJson(response));
            }
        } catch (IllegalArgumentException | EntryNotProcessedException e) {
            LOGGER.error("Could not save expense. Error: {}.", e.getLocalizedMessage(), e);
            return ResponseEntity.badRequest().body(String.format("Could not save expense. Error: %s.", e.getMessage()));
        }
        throw new InternalServerError("An unexpected Error occurred. Could not save expense.");
    }

    @Operation(
            description = "Updates an expense in JSON format if the specified expense exists. Returns the expense if successful",
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
    public ResponseEntity<String> update(@RequestBody String jsonExpense) {
        Expense expense = this.GSON.fromJson(jsonExpense, Expense.class);
        try {
            ValidateEntryHelper.isValid(expense);
            boolean isUpdated = this.EXPENSE_SERVICE.update(expense);
            if (isUpdated) {
                String message = String.format("Expense with id %d was updated successfully.", expense.getId());
                Map<String, Object> response = ValidateEntryHelper.buildResponseBody(message, expense);
                LOGGER.info(message);
                return ResponseEntity.ok(this.GSON.toJson(response));
            }
        } catch (IllegalArgumentException | EntryNotProcessedException e) {
            LOGGER.error("Could not update expense. Error: {}.", e.getLocalizedMessage(), e);
            return ResponseEntity.badRequest().body(String.format("Could not update expense. Error: %s.", e.getMessage()));
        } catch (EntryNotFoundException e) {
            LOGGER.error("Could not find expense to update. Error: {}.", e.getLocalizedMessage(), e);
            return ResponseEntity.notFound().build();
        }
        throw new InternalServerError("An unexpected Error occurred. Could not update expense.");
    }

    @Operation(
            description = "Deletes an expense with a given ID",
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
        boolean isDeleted = this.EXPENSE_SERVICE.delete(id);
        if (isDeleted) {
            String message = String.format("Expense with id %d was deleted successfully.", id);
            LOGGER.info(message);
            return ResponseEntity.ok(message);
        }
        String message = String.format("Expense with id %d could not be deleted.", id);
        LOGGER.info(message);
        return ResponseEntity.badRequest().body(message);
    }
}
