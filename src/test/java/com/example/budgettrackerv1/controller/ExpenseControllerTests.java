package com.example.budgettrackerv1.controller;

import com.example.budgettrackerv1.model.Category;
import com.example.budgettrackerv1.model.Expense;
import com.example.budgettrackerv1.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseController.class)
public class ExpenseControllerTests {

    @MockBean
    private ExpenseService expenseService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void getAllExpenses_Success() throws Exception {
        Expense expense = new Expense();
        expense.setId(1);
        expense.setDateCreated(new Date());
        expense.setDatePlanned(new Date());
        expense.setCategory(Category.GROCERIES);
        expense.setDescription("Test expense");
        expense.setAmount(10);

        List<Expense> expenses = List.of(expense);

        Mockito.when(expenseService.getExpenses()).thenReturn(expenses);

        mockMvc.perform(get("/expenses"))
                .andExpect(status().isOk());
    }

}
