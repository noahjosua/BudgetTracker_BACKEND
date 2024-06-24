package com.example.budgettrackerv1.service;

import com.example.budgettrackerv1.exception.EntryNotFoundException;
import com.example.budgettrackerv1.exception.EntryNotProcessedException;
import com.example.budgettrackerv1.model.Category;
import com.example.budgettrackerv1.model.Expense;
import com.example.budgettrackerv1.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTests {

    @Mock
    ExpenseRepository expenseRepository;

    @InjectMocks
    ExpenseService expenseService;

    private List<Expense> testExpenses;

    @BeforeEach
    public void setUp() {
        Expense expense1 = new Expense();
        expense1.setId(1);
        expense1.setDateCreated(LocalDate.now());
        expense1.setDatePlanned(LocalDate.of(2023, 1, 15));
        expense1.setCategory(Category.GROCERIES);
        expense1.setDescription("Test expense");
        expense1.setAmount(10);

        Expense expense2 = new Expense();
        expense2.setId(2);
        expense2.setDateCreated(LocalDate.now());
        expense2.setDatePlanned(LocalDate.of(2023, 1, 20));
        expense2.setCategory(Category.GROCERIES);
        expense2.setDescription("Test expense");
        expense2.setAmount(10);

        testExpenses = Arrays.asList(expense1, expense2);
    }

    @Test
    @DisplayName("Get Expenses By Date - Returns Expected Expenses")
    void getByDate_ReturnsExpenses() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        when(expenseRepository.findAllByLocalDatePlannedBetween(startDate, endDate))
                .thenReturn(Optional.of(testExpenses));

        Optional<List<Expense>> actualExpenses = expenseService.getByDate(startDate, endDate);

        assertEquals(Optional.of(testExpenses), actualExpenses);
    }

    @Test
    @DisplayName("Get Expenses By Date - Returns Empty List")
    void getByDate_ReturnsEmptyList() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        when(expenseRepository.findAllByLocalDatePlannedBetween(startDate, endDate))
                .thenReturn(Optional.empty());

        Optional<List<Expense>> actualExpenses = expenseService.getByDate(startDate, endDate);

        assertEquals(Optional.empty(), actualExpenses);
    }

    @Test
    @DisplayName("Save Expense - Success")
    void save_Success() {
        Expense expense = testExpenses.get(1);
        when(expenseRepository.save(expense)).thenReturn(expense);

        boolean isSaved = expenseService.save(expense);

        assertTrue(isSaved);
        verify(expenseRepository, times(1)).save(expense);
    }

    @Test
    @DisplayName("Save Expense - Throws EntryNotProcessedException")
    void save_ThrowsIllegalArgumentException() {
        Expense expense = testExpenses.get(1);
        String errorMessage = "Invalid argument";
        when(expenseRepository.save(expense)).thenThrow(new IllegalArgumentException(errorMessage));

        EntryNotProcessedException exception = assertThrows(EntryNotProcessedException.class, () -> {
            expenseService.save(expense);
        });

        assertEquals("Could not save expense. Error: " + errorMessage, exception.getMessage());
        verify(expenseRepository, times(1)).save(expense);
    }

    @Test
    @DisplayName("Update Expense - Success")
    void update_Success() {
        Expense expense = testExpenses.get(1);
        Optional<Expense> existingExpenseOptional = Optional.of(expense);
        when(expenseRepository.findById(expense.getId())).thenReturn(existingExpenseOptional);
        when(expenseRepository.save(expense)).thenReturn(expense);

        boolean isUpdated = expenseService.update(expense);

        assertTrue(isUpdated);
        verify(expenseRepository, times(1)).delete(expense);
        verify(expenseRepository, times(1)).save(expense);
    }

    @Test
    @DisplayName("Update Expense - Throws EntryNotFoundException")
    void update_ThrowsEntryNotFoundException() {
        Expense expense = testExpenses.get(1);
        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.empty());

        EntryNotFoundException exception = assertThrows(EntryNotFoundException.class, () -> {
            expenseService.update(expense);
        });

        assertEquals("Expense not found. Could not update expense.", exception.getMessage());
        verify(expenseRepository, never()).delete(Mockito.any(Expense.class));
        verify(expenseRepository, never()).save(any(Expense.class));
    }

    @Test
    @DisplayName("Update Expense - Throws EntryNotProcessedException")
    void update_ThrowsEntryNotProcessedException() {
        Expense expense = testExpenses.get(1);
        String errorMessage = "Some processing error";
        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.of(expense));
        doThrow(new IllegalArgumentException(errorMessage)).when(expenseRepository).delete(expense);

        EntryNotProcessedException exception = assertThrows(EntryNotProcessedException.class, () -> {
            expenseService.update(expense);
        });

        assertEquals("Could not delete expense. Error: " + errorMessage, exception.getMessage());
        verify(expenseRepository, times(1)).delete(expense);
        verify(expenseRepository, never()).save(expense);
    }

    @Test
    @DisplayName("Delete Expense - Success")
    void delete_Success() {
        Expense expense = testExpenses.get(1);
        Optional<Expense> existingExpenseOptional = Optional.of(expense);
        when(expenseRepository.findById(expense.getId())).thenReturn(existingExpenseOptional);

        boolean isDeleted = expenseService.delete(expense.getId());

        assertTrue(isDeleted);
        verify(expenseRepository, times(1)).delete(expense);
    }

    @Test
    @DisplayName("Delete Expense - Throws EntryNotFoundException")
    void delete_ThrowsEntryNotFoundException() {
        Expense expense = testExpenses.get(1);
        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.empty());

        EntryNotFoundException exception = assertThrows(EntryNotFoundException.class, () -> {
            expenseService.delete(expense.getId());
        });

        assertEquals("Expense not found. Could not delete expense.", exception.getMessage());
        verify(expenseRepository, never()).delete(Mockito.any(Expense.class));
    }

    @Test
    @DisplayName("Delete Expense - Throws EntryNotProcessedException")
    void delete_ThrowsEntryNotProcessedException() {
        int expenseId = 1;
        when(expenseRepository.findById(expenseId)).thenThrow(IllegalArgumentException.class);

        EntryNotProcessedException exception = assertThrows(EntryNotProcessedException.class, () -> {
            expenseService.delete(expenseId);
        });

        assertNotNull(exception.getMessage());
        verify(expenseRepository, never()).delete(Mockito.any(Expense.class));
    }
}
