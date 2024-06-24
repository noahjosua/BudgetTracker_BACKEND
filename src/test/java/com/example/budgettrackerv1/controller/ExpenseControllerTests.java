package com.example.budgettrackerv1.controller;

import com.example.budgettrackerv1.Constants;
import com.example.budgettrackerv1.adapter.LocalDateTypeAdapter;
import com.example.budgettrackerv1.exception.EntryNotFoundException;
import com.example.budgettrackerv1.exception.EntryNotProcessedException;
import com.example.budgettrackerv1.helper.GetEntriesByDateHelper;
import com.example.budgettrackerv1.helper.ValidateEntryHelper;
import com.example.budgettrackerv1.model.Category;
import com.example.budgettrackerv1.model.ErrorResponse;
import com.example.budgettrackerv1.model.Expense;
import com.example.budgettrackerv1.service.ExpenseService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseController.class)
public class ExpenseControllerTests {

    @MockBean
    private ExpenseService expenseService;

    @Autowired
    private MockMvc mockMvc;

    private final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .create();

    private Expense testExpense;

    @BeforeEach
    public void setUp() {
        testExpense = new Expense();
        testExpense.setId(1);
        testExpense.setDateCreated(LocalDate.now());
        testExpense.setDatePlanned(LocalDate.now());
        testExpense.setCategory(Category.GROCERIES);
        testExpense.setDescription("Test expense");
        testExpense.setAmount(10);
    }

    @Test
    @DisplayName("Get Categories - Returns Status 200")
    void getAllCategories_Status_200() throws Exception {
        List<Category> expectedCategories = Arrays.asList(
                Category.GROCERIES, Category.DRUGSTORE, Category.FREE_TIME, Category.RENT, Category.INSURANCE, Category.SUBSCRIPTIONS, Category.EDUCATION, Category.OTHER
        );

        mockMvc.perform(get("/api/expenses/categories"))
                .andExpect(status().isOk())
                .andExpect(content().string(GSON.toJson(expectedCategories)));
    }

    @Test
    @DisplayName("Get All Expenses By Date - Returns Status 200")
    void getExpensesByDate_Status_200() throws Exception {
        List<Expense> expenses = List.of(testExpense);
        when(expenseService.getByDate(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(Optional.of(expenses));

        String messageSuccess = GetEntriesByDateHelper.getSuccessMessageForByDateRequest(new Date(), Constants.TYPE_EXPENSES);
        Map<String, Object> expectedResponse = ValidateEntryHelper.buildResponseBody(messageSuccess, expenses);
        String jsonExpectedResponse = GSON.toJson(expectedResponse);

        mockMvc.perform(get("/api/expenses/byDate/" + LocalDate.now()))
                .andExpect(status().isOk())
                .andExpect(content().string(jsonExpectedResponse));
    }

    @Test
    @DisplayName("Get All Expenses By Date - Returns Status 404")
    void getExpensesByDate_Status_404() throws Exception {
        when(expenseService.getByDate(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/expenses/byDate/" + LocalDate.now()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Save Expense - Returns Status 200")
    void save_Status_200() throws Exception {
        String jsonExpense = GSON.toJson(testExpense);
        when(expenseService.save(Mockito.any(Expense.class))).thenReturn(true);

        String message = String.format("Expense with id %d was saved successfully.", testExpense.getId());
        Map<String, Object> response = ValidateEntryHelper.buildResponseBody(message, testExpense);
        String responseJson = GSON.toJson(response);

        mockMvc.perform(post("/api/expenses/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonExpense))
                .andExpect(status().isOk())
                .andExpect(content().string(responseJson));
    }

    @Test
    @DisplayName("Save Expense - Returns Status 400")
    void save_Status_400() throws Exception {
        testExpense.setDescription("");
        String jsonExpense = GSON.toJson(testExpense);
        when(expenseService.save(Mockito.any(Expense.class))).thenReturn(false);

        mockMvc.perform(post("/api/expenses/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonExpense))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Could not save expense. Error: Description cannot be null or empty."));
    }

    @Test
    @DisplayName("Save Expense - Returns Status 500")
    void save_Status_500() throws Exception {
        String jsonExpense = GSON.toJson(testExpense, Expense.class);
        when(expenseService.save(Mockito.any(Expense.class))).thenReturn(false);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("An unexpected Error occurred. Could not save expense.");
        errorResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        String responseJson = GSON.toJson(errorResponse);

        mockMvc.perform(post("/api/expenses/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonExpense))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(responseJson));
    }

    @Test
    @DisplayName("Update Expense - Returns Status 200")
    void update_Status_200() throws Exception {
        String jsonExpense = GSON.toJson(testExpense);
        when(expenseService.update(Mockito.any(Expense.class))).thenReturn(true);

        String message = String.format("Expense with id %d was updated successfully.", testExpense.getId());
        Map<String, Object> response = ValidateEntryHelper.buildResponseBody(message, testExpense);
        String responseJson = GSON.toJson(response);

        mockMvc.perform(put("/api/expenses/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonExpense))
                .andExpect(status().isOk())
                .andExpect(content().string(responseJson));
    }

    @Test
    @DisplayName("Update Expense - Returns Status 400")
    void update_Status_400() throws Exception {
        testExpense.setDescription("");
        String jsonExpense = GSON.toJson(testExpense);

        when(expenseService.update(Mockito.any(Expense.class))).thenThrow(new EntryNotProcessedException("Description cannot be null or empty."));
        when(expenseService.update(Mockito.any(Expense.class))).thenThrow(new IllegalArgumentException("Description cannot be null or empty."));

        mockMvc.perform(put("/api/expenses/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonExpense))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Could not update expense. Error: Description cannot be null or empty."));
    }

    @Test
    @DisplayName("Update Expense - Returns Status 404")
    void update_Status_404() throws Exception {
        testExpense.setId(-1);
        String jsonExpense = GSON.toJson(testExpense);

        when(expenseService.update(Mockito.any(Expense.class))).thenThrow(new EntryNotFoundException("Expense not found. Could not update expense."));

        mockMvc.perform(put("/api/expenses/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonExpense))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update Expense - Returns Status 500")
    void update_Status_500() throws Exception {
        String jsonExpense = GSON.toJson(testExpense);

        when(expenseService.update(Mockito.any(Expense.class))).thenReturn(false);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("An unexpected Error occurred. Could not update expense.");
        errorResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        String responseJson = GSON.toJson(errorResponse);

        mockMvc.perform(put("/api/expenses/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonExpense))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(responseJson));
    }

    @Test
    @DisplayName("Delete Expense - Returns Status 200")
    void delete_Status_200() throws Exception {
        int expenseId = 1;
        when(expenseService.delete(expenseId)).thenReturn(true);

        String expectedMessage = String.format("Expense with id %d was deleted successfully.", expenseId);

        mockMvc.perform(delete("/api/expenses/delete/" + expenseId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMessage));
    }

    @Test
    @DisplayName("Delete Expense - Returns Status 400")
    void delete_Status_400() throws Exception {
        int expenseId = 1;
        when(expenseService.delete(expenseId)).thenThrow(new EntryNotProcessedException("Could not delete expense due to processing error."));

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Could not delete expense due to processing error.");
        errorResponse.setCode(HttpStatus.BAD_REQUEST.value());
        String responseJson = GSON.toJson(errorResponse);

        mockMvc.perform(delete("/api/expenses/delete/" + expenseId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(responseJson));
    }

    @Test
    @DisplayName("Delete Expense - Returns Status 404")
    void delete_Status_404() throws Exception {
        int expenseId = 1;
        when(expenseService.delete(expenseId)).thenThrow(new EntryNotFoundException("Expense not found. Could not delete expense."));

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Expense not found. Could not delete expense.");
        errorResponse.setCode(HttpStatus.NOT_FOUND.value());
        String responseJson = GSON.toJson(errorResponse);

        mockMvc.perform(delete("/api/expenses/delete/" + expenseId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(responseJson));
    }
}
