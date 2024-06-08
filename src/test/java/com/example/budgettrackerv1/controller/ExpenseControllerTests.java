package com.example.budgettrackerv1.controller;

import com.example.budgettrackerv1.Constants;
import com.example.budgettrackerv1.adapter.LocalDateTypeAdapter;
import com.example.budgettrackerv1.helper.Helper;
import com.example.budgettrackerv1.model.Category;
import com.example.budgettrackerv1.model.Expense;
import com.example.budgettrackerv1.service.ExpenseService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Mock
    private final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .create();

    private Expense createTestExpense() {
        Expense expense = new Expense();
        expense.setId(1);
        expense.setDateCreated(LocalDate.now());
        expense.setDatePlanned(LocalDate.now());
        expense.setCategory(Category.GROCERIES);
        expense.setDescription("Test expense");
        expense.setAmount(10);
        return expense;
    }

    @Test
    void getCategories_Status_200() throws Exception {
        // Arrange
        List<Category> expectedCategories = Arrays.asList(
                Category.GROCERIES, Category.DRUGSTORE, Category.FREE_TIME, Category.RENT, Category.INSURANCE, Category.SUBSCRIPTIONS, Category.EDUCATION, Category.OTHER
        );

        // Act & Assert
        mockMvc.perform(get("/api/expenses/categories"))
                .andExpect(status().isOk())
                .andExpect(content().string(GSON.toJson(expectedCategories)));
    }

    @Test
    void getAllExpensesByDate_Status_200() throws Exception {
        // Arrange
        Expense expense = createTestExpense();

        List<Expense> expenses = List.of(expense);
        Optional<LocalDate> startDate = Helper.getDate(new Date(), Constants.FIRST_DAY_KEY);
        Optional<LocalDate> endDate = Helper.getDate(new Date(), Constants.LAST_DAY_KEY);

        if (startDate.isPresent() && endDate.isPresent()) {
            when(expenseService.getByDate(startDate.get(), endDate.get())).thenReturn(Optional.of(expenses));

            String messageSuccess = Helper.getSuccessMessageForByDateRequest(new Date(), Constants.TYPE_EXPENSES);
            Map<String, Object> expectedResponse = Map.of(
                    Constants.RESPONSE_MESSAGE_KEY, messageSuccess,
                    Constants.RESPONSE_ENTRY_KEY, expenses
            );

            // Act & Assert
            mockMvc.perform(get("/api/expenses/byDate/" + LocalDate.now()))
                    .andExpect(status().isOk())
                    .andExpect(content().string(GSON.toJson(expectedResponse)));
        }
    }

    @Test
    void getAllExpensesByDate_Status_404() throws Exception {
        // Arrange
        Optional<LocalDate> startDate = Helper.getDate(new Date(), Constants.FIRST_DAY_KEY);
        Optional<LocalDate> endDate = Helper.getDate(new Date(), Constants.LAST_DAY_KEY);

        if (startDate.isPresent() && endDate.isPresent()) {
            when(expenseService.getByDate(startDate.get(), endDate.get())).thenReturn(Optional.empty());

            // Act & Assert
            mockMvc.perform(get("/api/expenses/byDate/" + LocalDate.now()))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    void getExpenseById_Status_200() throws Exception {
        // Arrange
        Expense expense = createTestExpense();
        Optional<Expense> expectedExpenseOptional = Optional.of(expense);
        when(expenseService.getById(expense.getId())).thenReturn(expectedExpenseOptional);

        // Act & Assert
        mockMvc.perform(get("/api/expenses/byId/" + expense.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void getExpenseById_Status_404() throws Exception {
        // Arrange
        Expense expense = createTestExpense();
        when(expenseService.getById(expense.getId())).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/expenses/byId/" + expense.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveExpense_Status_200() throws Exception {
        // Arrange
        Expense expense = createTestExpense();
        assertNotNull(expense, "The expense object should not be null");
        System.out.println(expense);
        String expenseJson = GSON.toJson(expense, Expense.class);
        assertNotNull(expenseJson, "The converted JSON string should not be null");
        /*
        when(expenseService.save(expense)).thenReturn(true);

        String message = String.format("Expense with id %d was saved successfully.", expense.getId());
        Map<String, Object> response = Map.of(
                Constants.RESPONSE_MESSAGE_KEY, message,
                Constants.RESPONSE_ENTRY_KEY, expense
        );

        // Act & Assert
        mockMvc.perform(post("/api/expenses/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(GSON.toJson(expense)))
                .andExpect(status().isOk())
                .andExpect(content().string(GSON.toJson(response)));

         */
    }

    @Test
    void saveExpense_Status_400() throws Exception {

    }
}
