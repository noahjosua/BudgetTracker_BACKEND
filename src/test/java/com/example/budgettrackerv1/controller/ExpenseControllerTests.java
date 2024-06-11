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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
        List<Category> expectedCategories = Arrays.asList(
                Category.GROCERIES, Category.DRUGSTORE, Category.FREE_TIME, Category.RENT, Category.INSURANCE, Category.SUBSCRIPTIONS, Category.EDUCATION, Category.OTHER
        );

        mockMvc.perform(get("/api/expenses/categories"))
                .andExpect(status().isOk())
                .andExpect(content().string(GSON.toJson(expectedCategories)));
    }

    @Test
    void getAllExpensesByDate_Status_200() throws Exception {
        List<Expense> expenses = List.of(createTestExpense());
        when(expenseService.getByDate(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(Optional.of(expenses));

        String messageSuccess = Helper.getSuccessMessageForByDateRequest(new Date(), Constants.TYPE_EXPENSES);
        Map<String, Object> expectedResponse = Map.of(
                Constants.RESPONSE_MESSAGE_KEY, messageSuccess,
                Constants.RESPONSE_ENTRY_KEY, expenses
        );
        String jsonExpectedResponse = GSON.toJson(expectedResponse);

        mockMvc.perform(get("/api/expenses/byDate/" + LocalDate.now()))
                .andExpect(status().isOk())
                .andExpect(content().string(jsonExpectedResponse));
    }

    @Test
    void getAllExpensesByDate_Status_404() throws Exception {
        when(expenseService.getByDate(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/expenses/byDate/" + LocalDate.now()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getExpenseById_Status_200() throws Exception {
        Expense expense = createTestExpense();
        Optional<Expense> expectedExpenseOptional = Optional.of(expense);
        when(expenseService.getById(Mockito.any(Integer.class))).thenReturn(expectedExpenseOptional);

        mockMvc.perform(get("/api/expenses/byId/" + expense.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void getExpenseById_Status_404() throws Exception {
        Expense expense = createTestExpense();
        when(expenseService.getById(Mockito.any(Integer.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/expenses/byId/" + expense.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveExpense_Status_200() throws Exception {
        Expense expense = createTestExpense();
        String jsonExpense = GSON.toJson(expense);
        when(expenseService.save(Mockito.any(Expense.class))).thenReturn(true);

        String message = String.format("Expense with id %d was saved successfully.", expense.getId());
        Map<String, Object> response = Map.of(
                Constants.RESPONSE_MESSAGE_KEY, message,
                Constants.RESPONSE_ENTRY_KEY, expense
        );
        String responseJson = GSON.toJson(response);

        mockMvc.perform(post("/api/expenses/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonExpense))
                .andExpect(status().isOk())
                .andExpect(content().string(responseJson));
    }

    @Test
    void saveExpense_Status_400() throws Exception {
        Expense expense = createTestExpense();
        String expenseJson = GSON.toJson(expense, Expense.class);
        when(expenseService.save(Mockito.any(Expense.class))).thenReturn(false);

        String message = String.format("Expense with id %d could not be saved.", expense.getId());

        mockMvc.perform(post("/api/expenses/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expenseJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(message));
    }

    @Test
    void updateExpense_Status_200() throws Exception {

    }

    @Test
    void updateExpense_Status_404() throws Exception {

    }

    @Test
    void deleteExpense_Status_200() throws Exception {

    }

    @Test
    void deleteExpense_Status_404() throws Exception {

    }
}
