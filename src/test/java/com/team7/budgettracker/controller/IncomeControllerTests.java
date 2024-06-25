package com.team7.budgettracker.controller;

import com.team7.budgettracker.Constants;
import com.team7.budgettracker.adapter.LocalDateTypeAdapter;
import com.team7.budgettracker.exception.EntryNotFoundException;
import com.team7.budgettracker.exception.EntryNotProcessedException;
import com.team7.budgettracker.helper.GetEntriesByDateHelper;
import com.team7.budgettracker.helper.ValidateEntryHelper;
import com.team7.budgettracker.model.Category;
import com.team7.budgettracker.model.ErrorResponse;
import com.team7.budgettracker.model.Income;
import com.team7.budgettracker.service.IncomeService;
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

@WebMvcTest(IncomeController.class)
public class IncomeControllerTests {

    @MockBean
    IncomeService incomeService;

    @Autowired
    MockMvc mockMvc;

    final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .create();

    Income testIncome;

    @BeforeEach
    public void setUp() {
        testIncome = new Income();
        testIncome.setAmount(13.50);
        testIncome.setCategory(Category.SALARY);
        testIncome.setDescription("Job");
        testIncome.setId(1);
        testIncome.setDateCreated(LocalDate.now());
        testIncome.setDatePlanned(LocalDate.now());
    }

    @Test
    @DisplayName("Get Categories - Status 200")
    void getAllCategories_Status_200() throws Exception {

        List<Category> expected = List.of(Category.SALARY, Category.POCKET_MONEY, Category.ALIMENT, Category.CAPITAL_ASSETS,
                Category.RENTAL, Category.OTHER);


        mockMvc.perform(get("/api/incomes/categories"))
                .andExpect(status().isOk())
                .andExpect(content().string(GSON.toJson(expected)));
    }

    @Test
    @DisplayName("Get Incomes by date - Status 200")
    void getIncomesByDate_Status_200() throws Exception {

        List<Income> expectedList = List.of(testIncome);

        when(incomeService.getByDate(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(Optional.of(expectedList));

        String messageSuccess = GetEntriesByDateHelper.getSuccessMessageForByDateRequest(new Date(), Constants.TYPE_INCOMES);
        Map<String, Object> expectedResponse = ValidateEntryHelper.buildResponseBody(messageSuccess, expectedList);
        String jsonExpectedResponse = GSON.toJson(expectedResponse);

        mockMvc.perform(get("/api/incomes/byDate/" + LocalDate.now())).andExpect(status().isOk()).andExpect(content().
                string(jsonExpectedResponse));
    }

    @Test
    @DisplayName("Get Incomes by date - Status 404")
    void getIncomesByDate_Status_404() throws Exception {
        when(incomeService.getByDate(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenThrow(EntryNotFoundException.class);

        mockMvc.perform(get("/api/incomes/byDate/" + LocalDate.now())).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Save Income - Status 200")
    void save_Status_200() throws Exception {

        String jsonIncome = GSON.toJson(testIncome);

        when(incomeService.save(Mockito.any(Income.class))).thenReturn(true);

        String message = String.format("Income with id %d was saved successfully.", testIncome.getId());
        Map<String, Object> response = ValidateEntryHelper.buildResponseBody(message, testIncome);
        String responseJson = GSON.toJson(response);

        mockMvc.perform(post("/api/incomes/save").contentType(MediaType.APPLICATION_JSON).content(jsonIncome))
                .andExpect(status().isOk())
                .andExpect(content().string(responseJson));
    }

    @Test
    @DisplayName("Save Income - Status 400")
    void save_Status_400() throws Exception {

        testIncome.setAmount(-1);
        String jsonIncome = GSON.toJson(testIncome);
        when(incomeService.save(Mockito.any(Income.class))).thenThrow(EntryNotProcessedException.class, IllegalArgumentException.class);

        mockMvc.perform(post("/api/incomes/save").contentType(MediaType.APPLICATION_JSON).content(jsonIncome))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Could not save income. Error: Amount cannot be negative."));
    }

    @Test
    @DisplayName("Save Income - Status 500")
    void save_Status_500() throws Exception {

        String jsonIncome = GSON.toJson(testIncome);

        when(incomeService.save(Mockito.any(Income.class))).thenReturn(false);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("An unexpected Error occurred. Could not save income.");
        errorResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        String expectedResponse = GSON.toJson(errorResponse);

        mockMvc.perform(post("/api/incomes/save").contentType(MediaType.APPLICATION_JSON).content(jsonIncome))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    @DisplayName("Update Income - Status 200")
    void update_Status_200() throws Exception {

        String jsonIncome = GSON.toJson(testIncome);

        when(incomeService.update(Mockito.any(Income.class))).thenReturn(true);

        String message = String.format("Income with id %d was updated successfully.", testIncome.getId());
        Map<String, Object> response = ValidateEntryHelper.buildResponseBody(message, testIncome);
        String expectedResponse = GSON.toJson(response);

        mockMvc.perform(put("/api/incomes/update").contentType(MediaType.APPLICATION_JSON).content(jsonIncome))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    @DisplayName("Update Income - Status 400")
    void update_Status_400() throws Exception {

        testIncome.setAmount(-1);

        String jsonIncome = GSON.toJson(testIncome);

        when(incomeService.update(Mockito.any(Income.class))).thenThrow(EntryNotProcessedException.class, IllegalArgumentException.class);

        mockMvc.perform(put("/api/incomes/update")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonIncome))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Could not update income. Error: Amount cannot be negative."));
    }

    @Test
    @DisplayName("Update Income - Status 500")
    void update_Status_500() throws Exception {

        String jsonIncome = GSON.toJson(testIncome);

        when(incomeService.update(Mockito.any(Income.class))).thenReturn(false);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("An unexpected Error occurred. Could not update income.");
        errorResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        String responseJson = GSON.toJson(errorResponse);

        mockMvc.perform(put("/api/incomes/update").contentType(MediaType.APPLICATION_JSON).content(jsonIncome))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(responseJson));
    }

    @Test
    @DisplayName("Delete Income - Status 200")
    void delete_Status_200() throws Exception {

        int id = 1;
        when(incomeService.delete(id)).thenReturn(true);

        String expectedMessage = String.format("Income with id %d was deleted successfully.", id);

        mockMvc.perform(delete("/api/incomes/delete/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMessage));
    }

    @Test
    @DisplayName("Delete Income - Status 400")
    void delete_Status_400() throws Exception {

        int id = 1;

        when(incomeService.delete(id)).thenThrow(new EntryNotProcessedException("Could not delete income due to processing error."));

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Could not delete income due to processing error.");
        errorResponse.setCode(HttpStatus.BAD_REQUEST.value());
        String expectedResponse = GSON.toJson(errorResponse);

        mockMvc.perform(delete("/api/incomes/delete/" + id))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    @DisplayName("Delete Income - Status 404")
    void delete_Status_404() throws Exception {

        int id = 1;

        when(incomeService.delete(id)).thenThrow(new EntryNotFoundException("Income not found. Could not delete income."));

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Income not found. Could not delete income.");
        errorResponse.setCode(HttpStatus.NOT_FOUND.value());
        String expectedResponse = GSON.toJson(errorResponse);

        mockMvc.perform(delete("/api/incomes/delete/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedResponse));
    }

}