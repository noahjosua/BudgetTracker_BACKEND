package com.example.budgettrackerv1.repository;

import com.example.budgettrackerv1.model.Category;
import com.example.budgettrackerv1.model.Expense;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ExpenseRepositoryTests {

    @Autowired
    ExpenseRepository expenseRepository;

    @Test
    public void testCreateReadDelete() {
        Expense expense = new Expense();
        expense.setDateCreated(LocalDate.now());
        expense.setDatePlanned(LocalDate.now());
        expense.setCategory(Category.GROCERIES);
        expense.setDescription("Test expense");
        expense.setAmount(10);

        expenseRepository.save(expense);
        Expense result = expenseRepository.findById(expense.getId()).get();
        assertThat(result.getDescription()).isEqualTo("Test expense");

        expenseRepository.deleteAll();
        assertThat(expenseRepository.findAll()).isEmpty();
    }
}
