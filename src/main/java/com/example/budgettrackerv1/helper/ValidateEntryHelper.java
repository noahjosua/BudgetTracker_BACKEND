package com.example.budgettrackerv1.helper;

import com.example.budgettrackerv1.Constants;
import com.example.budgettrackerv1.model.Category;
import com.example.budgettrackerv1.model.Expense;
import com.example.budgettrackerv1.model.Income;
import java.util.*;

public class ValidateEntryHelper {

    public static void isValidEntry(Income income) {
        if (income.getDateCreated() == null) {
            throw new IllegalArgumentException("Date of creation cannot be null.");
        }
        if (income.getDatePlanned() == null) {
            throw new IllegalArgumentException("Planned Date cannot be null.");
        }
        if (Category.SALARY != income.getCategory() && Category.POCKET_MONEY != income.getCategory() &&
                Category.ALIMENT != income.getCategory() && Category.CAPITAL_ASSETS != income.getCategory() &&
                Category.RENTAL != income.getCategory()) {
            throw new IllegalArgumentException("Chosen Category is not valid.");
        }
        if (income.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        if (income.getDescription() == null || income.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
    }

    public static void isValidEntry(Expense expense) {
        if (expense.getDateCreated() == null) {
            throw new IllegalArgumentException("Date of creation cannot be null.");
        }
        if (expense.getDatePlanned() == null) {
            throw new IllegalArgumentException("Planned Date cannot be null.");
        }
        if (Category.GROCERIES != expense.getCategory() && Category.DRUGSTORE != expense.getCategory() &&
                Category.FREE_TIME != expense.getCategory() && Category.RENT != expense.getCategory() &&
                Category.INSURANCE != expense.getCategory() && Category.SUBSCRIPTIONS != expense.getCategory() &&
                Category.EDUCATION != expense.getCategory() && Category.OTHER != expense.getCategory()) {
            throw new IllegalArgumentException("Chosen Category is not valid.");
        }
        if (expense.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        if (expense.getDescription() == null || expense.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
    }

    public static Map<String, Object> buildResponseBody(String message, Object entry) {
        return Map.of(
                Constants.RESPONSE_MESSAGE_KEY, message,
                Constants.RESPONSE_ENTRY_KEY, entry
        );
    }
}
