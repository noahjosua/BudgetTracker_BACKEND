package com.example.budgettrackerv1;

import com.example.budgettrackerv1.controller.ExpenseController;
import com.example.budgettrackerv1.controller.IncomeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BudgetTrackerV1ApplicationTests {

	@Autowired
	ExpenseController expenseController;

	@Autowired
	IncomeController incomeController;

	/**
	 * Check if the beans have been successfully injected into an autowired attribute or not
	 */
	@Test
	void contextLoads() {
		assertThat(expenseController).isNotNull();
		assertThat(incomeController).isNotNull();
	}
}
