package com.example.budgettrackerv1.service;

import com.example.budgettrackerv1.model.Category;
import com.example.budgettrackerv1.model.Expense;
import com.example.budgettrackerv1.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTests {

    ExpenseService expenseService;

    @Mock
    ExpenseRepository expenseRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        expenseService = new ExpenseService(expenseRepository);
    }

    @Test
    void testGetAllExpenses_Success() {
        Expense expense = new Expense();
        expense.setId(1);
        expense.setDateCreated(LocalDate.now());
        expense.setDatePlanned(LocalDate.now());
        expense.setCategory(Category.GROCERIES);
        expense.setDescription("Test expense");
        expense.setAmount(10);

        List<Expense> expenses = List.of(expense);
        when(expenseRepository.findAll()).thenReturn(expenses);

        // test
        //List<Expense> retrievedExpenses = expenseService.getExpenses();
        //assertEquals(1, retrievedExpenses.size());
       // verify(expenseRepository, times(1)).findAll();
    }
}
