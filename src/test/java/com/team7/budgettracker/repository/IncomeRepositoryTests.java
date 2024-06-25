package com.team7.budgettracker.repository;

import com.team7.budgettracker.model.Category;
import com.team7.budgettracker.model.Income;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class IncomeRepositoryTests {

    @Autowired
    IncomeRepository incomeRepository;

    Income test1;
    Income test2;

    @BeforeEach
    void setUp() {

        test1 = new Income();
        test1.setAmount(13.50);
        test1.setCategory(Category.SALARY);
        test1.setDescription("Job");
        test1.setId(1);
        test1.setDateCreated(LocalDate.now());
        test1.setDatePlanned(LocalDate.of(2024, 06, 30));

        test2 = new Income();
        test2.setAmount(100);
        test2.setCategory(Category.RENTAL);
        test2.setDescription("House");
        test2.setId(2);
        test2.setDateCreated(LocalDate.now());
        test2.setDatePlanned(LocalDate.of(2024, 07, 30));
    }

    @Test
    @DisplayName("Incomes Between Given Start and End Dates - Incomes found")
    void findIncomesByLocalDatePlannedBetween_ReturnIncomesInRange() {

        incomeRepository.save(test1);
        incomeRepository.save(test2);

        LocalDate start = LocalDate.of(2024, 5, 1);
        LocalDate end = LocalDate.of(2024, 7, 31);

        Optional<List<Income>> incomes = incomeRepository.findAllByLocalDatePlannedBetween(start, end);

        assertThat(incomes).isPresent();
        assertThat(incomes.get()).hasSize(2);
    }

    @Test
    @DisplayName("Incomes Between Given Start and End Dates - No Incomes")
    void findIncomesByLocalDatePlannedBetween_ReturnNoIncomes() {

        incomeRepository.save(test1);
        incomeRepository.save(test2);

        LocalDate start = LocalDate.of(2024, 5, 1);
        LocalDate end = LocalDate.of(2024, 5, 31);

        Optional<List<Income>> incomes = incomeRepository.findAllByLocalDatePlannedBetween(start, end);

        assertThat(incomes).isPresent();
        assertThat(incomes.get()).hasSize(0);
    }
}
