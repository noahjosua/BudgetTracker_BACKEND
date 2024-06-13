package com.example.budgettrackerv1.repository;

import com.example.budgettrackerv1.model.Category;
import com.example.budgettrackerv1.model.Expense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/* IMPORTANT: COMMENT OUT INSERT STATEMENTS IN SCHEMA.SQL BEFORE RUNNING TESTS */
@DataJpaTest
public class ExpenseRepositoryTests {

    @Autowired
    ExpenseRepository expenseRepository;

    private Expense testExpense1;
    private Expense testExpense2;
    private Expense testExpense3;

    @BeforeEach
    public void setUp() {
        testExpense1 = new Expense();
        testExpense1.setDateCreated(LocalDate.now());
        testExpense1.setDatePlanned(LocalDate.of(2024, 3, 15));
        testExpense1.setCategory(Category.GROCERIES);
        testExpense1.setDescription("Test expense 1");
        testExpense1.setAmount(10);

        testExpense2 = new Expense();
        testExpense2.setDateCreated(LocalDate.now());
        testExpense2.setDatePlanned(LocalDate.of(2024, 3, 20));
        testExpense2.setCategory(Category.GROCERIES);
        testExpense2.setDescription("Test expense 2");
        testExpense2.setAmount(20);

        testExpense3 = new Expense();
        testExpense3.setDateCreated(LocalDate.now());
        testExpense3.setDatePlanned(LocalDate.of(2024, 4, 1));
        testExpense3.setCategory(Category.GROCERIES);
        testExpense3.setDescription("Test expense 3");
        testExpense3.setAmount(30);
    }

    @Test
    @DisplayName("Find Expenses Between Given Start and End Dates - Returns Expected Expenses")
    void findExpensesByLocalDatePlannedBetween_ReturnExpensesInRange() {
        expenseRepository.save(testExpense1);
        expenseRepository.save(testExpense2);
        expenseRepository.save(testExpense3);

        LocalDate startDate = LocalDate.of(2024, 3, 1);
        LocalDate endDate = LocalDate.of(2024, 3, 31);

        Optional<List<Expense>> expensesInRange = expenseRepository.findAllByLocalDatePlannedBetween(startDate, endDate);

        assertThat(expensesInRange).isPresent();
        assertThat(expensesInRange.get()).hasSize(2);
        assertThat(expensesInRange.get()).containsExactlyInAnyOrder(testExpense1, testExpense2);
    }

    @Test
    @DisplayName("Find Expenses Between Given Start and End Dates - Returns No Expenses")
    void findExpensesByLocalDatePlannedBetween_ReturnNoExpenses() {
        expenseRepository.save(testExpense1);
        expenseRepository.save(testExpense2);
        expenseRepository.save(testExpense3);

        LocalDate startDate = LocalDate.of(2024, 5, 1);
        LocalDate endDate = LocalDate.of(2024, 5, 31);

        Optional<List<Expense>> expensesInRange = expenseRepository.findAllByLocalDatePlannedBetween(startDate, endDate);

        assertThat(expensesInRange).isPresent();
        assertThat(expensesInRange.get()).hasSize(0);
    }
}
