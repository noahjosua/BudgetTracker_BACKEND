package com.team7.budgettracker.service;

import com.team7.budgettracker.exception.EntryNotFoundException;
import com.team7.budgettracker.exception.EntryNotProcessedException;
import com.team7.budgettracker.model.Category;
import com.team7.budgettracker.model.Income;
import com.team7.budgettracker.repository.IncomeRepository;
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
public class IncomeServiceTests {

    @Mock
    IncomeRepository incomeRepository;

    @InjectMocks
    IncomeService incomeService;

    List<Income> incomesTest;

    @BeforeEach
    void setUp() {

        Income test1 = new Income();
        test1.setAmount(13.50);
        test1.setCategory(Category.SALARY);
        test1.setDescription("Job");
        test1.setId(1);
        test1.setDateCreated(LocalDate.now());
        test1.setDatePlanned(LocalDate.of(2024, 06, 30));

        Income test2 = new Income();
        test2.setAmount(100);
        test2.setCategory(Category.RENTAL);
        test2.setDescription("House");
        test2.setId(2);
        test2.setDateCreated(LocalDate.now());
        test2.setDatePlanned(LocalDate.of(2024, 07, 30));

        incomesTest = Arrays.asList(test1, test2);
    }

    @Test
    @DisplayName("Incomes Between Given Start and End Dates - Incomes found")
    void getByDate_ReturnsIncomes() {

        LocalDate start = LocalDate.of(2024, 5, 1);
        LocalDate end = LocalDate.of(2024, 7, 31);

        when(incomeRepository.findAllByLocalDatePlannedBetween(start, end)).thenReturn(Optional.of(incomesTest));

        Optional<List<Income>> actualIncomes = incomeService.getByDate(start, end);

        assertEquals(Optional.of(incomesTest), actualIncomes);
        assertEquals(2, actualIncomes.get().size());
    }

    @Test
    @DisplayName("Incomes Between Given Start and End Dates - Incomes not found")
    void getByDate_ReturnsEmptyList() {

        LocalDate start = LocalDate.of(2025, 5, 1);
        LocalDate end = LocalDate.of(2025, 7, 31);

        when(incomeRepository.findAllByLocalDatePlannedBetween(start, end)).thenReturn(Optional.empty());

        Optional<List<Income>> actualIncomes = incomeService.getByDate(start, end);

        assertEquals(Optional.empty(), actualIncomes);
    }

    @Test
    @DisplayName("Save Income - Success")
    void save_Success() {

        Income income = incomesTest.get(0);
        when(incomeRepository.save(income)).thenReturn(income);
        assertTrue(incomeService.save(income));

        verify(incomeRepository, times(1)).save(income);
    }

    @Test
    @DisplayName("Save Income - Fail")
    void save_ThrowsIllegalArgumentException() {

        Income income = incomesTest.get(0);
        String errorMessage = "Saving was unsuccessful";
        when(incomeRepository.save(income)).thenThrow(new IllegalArgumentException(errorMessage));

        EntryNotProcessedException e = assertThrows(EntryNotProcessedException.class, () -> {
            incomeService.save(income);
        });

        assertEquals("Could not save income. Error: " + errorMessage, e.getMessage());
        verify(incomeRepository, times(1)).save(income);

    }

    @Test
    @DisplayName("Update Income - Success")
    void update_Success() {

        Income income = incomesTest.get(0);
        Optional<Income> existingIncome = Optional.of(income);

        when(incomeRepository.findById(income.getId())).thenReturn(existingIncome);
        when(incomeRepository.save(income)).thenReturn(income);

        assertTrue(incomeService.update(income));
        verify(incomeRepository, times(1)).delete(income);
        verify(incomeRepository, times(1)).save(income);
    }

    @Test
    @DisplayName("Update Income - Fail Entry not Found")
    void update_ThrowsEntryNotFoundException() {

        Income income = incomesTest.get(0);

        when(incomeRepository.findById(income.getId())).thenReturn(Optional.empty());

        EntryNotFoundException e = assertThrows(EntryNotFoundException.class, () -> {
            incomeService.update(income);
        });

        assertEquals("Income not found. Could not update income.", e.getMessage());
        verify(incomeRepository, never()).delete(Mockito.any(Income.class));
        verify(incomeRepository, never()).save(any(Income.class));
    }

    @Test
    @DisplayName("Update Income - Fail Entry not Processed")
    void update_ThrowsEntryNotProcessedException() {

        Income income = incomesTest.get(0);
        String errorMessage = "Processing error";

        when(incomeRepository.findById(income.getId())).thenReturn(Optional.of(income));
        doThrow(new IllegalArgumentException(errorMessage)).when(incomeRepository).delete(income);

        EntryNotProcessedException e = assertThrows(EntryNotProcessedException.class, () -> {
            incomeService.update(income);
        });

        assertEquals("Could not delete income. Error: " + errorMessage, e.getMessage());
        verify(incomeRepository, times(1)).delete(income);
        verify(incomeRepository, never()).save(income);
    }

    @Test
    @DisplayName("Delete Income - Success")
    void delete_Success() {

        Income income = incomesTest.get(0);
        Optional<Income> existingIncome = Optional.of(income);

        when(incomeRepository.findById(income.getId())).thenReturn(existingIncome);

        assertTrue(incomeService.delete(income.getId()));
        verify(incomeRepository, times(1)).delete(income);
    }

    @Test
    @DisplayName("Delete Income - Fail Entry not Found")
    void delete_ThrowsEntryNotFoundException() {
        Income income = incomesTest.get(0);
        when(incomeRepository.findById(income.getId())).thenReturn(Optional.empty());

        EntryNotFoundException e = assertThrows(EntryNotFoundException.class, () -> {
            incomeService.delete(income.getId());
        });

        assertEquals("Income not found. Could not delete income.", e.getMessage());
        verify(incomeRepository, never()).delete(Mockito.any(Income.class));
    }

    @Test
    @DisplayName("Delete Income - Fail Entry not Processed")
    void delete_ThrowsEntryNotProcessedException() {

        int id = 1;
        when(incomeRepository.findById(id)).thenThrow(IllegalArgumentException.class);

        EntryNotProcessedException e = assertThrows(EntryNotProcessedException.class, () -> {
            incomeService.delete(id);
        });

        assertNotNull(e.getMessage());
        verify(incomeRepository, never()).delete(Mockito.any(Income.class));
    }


}
