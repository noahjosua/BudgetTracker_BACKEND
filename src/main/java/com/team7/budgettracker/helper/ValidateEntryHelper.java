package com.team7.budgettracker.helper;

import com.team7.budgettracker.Constants;
import com.team7.budgettracker.model.Category;
import com.team7.budgettracker.model.Entry;
import com.team7.budgettracker.model.Expense;
import com.team7.budgettracker.model.Income;

import java.util.*;

/**
 * Helper class for validating {@link Entry} objects.
 * Provides methods for building response bodies and validating entries.
 */
public class ValidateEntryHelper {

    private static final List<Category> VALID_INCOME_CATEGORIES = Arrays.asList(
            Category.SALARY, Category.POCKET_MONEY, Category.ALIMENT, Category.CAPITAL_ASSETS, Category.RENTAL
    );

    private static final List<Category> VALID_EXPENSE_CATEGORIES = Arrays.asList(
            Category.GROCERIES, Category.DRUGSTORE, Category.FREE_TIME,
            Category.RENT, Category.INSURANCE, Category.SUBSCRIPTIONS,
            Category.EDUCATION, Category.OTHER
    );

    /**
     * Builds a response body as a map with the given message and entry.
     *
     * @param message the response message
     * @param entry the entry object to include in the response
     * @return a map representing the response body
     */
    public static Map<String, Object> buildResponseBody(String message, Object entry) {
        return Map.of(
                Constants.RESPONSE_MESSAGE_KEY, message,
                Constants.RESPONSE_ENTRY_KEY, entry
        );
    }

    public static void isValid(Entry entry) {
        isValidEntry(entry);
    }

    /**
     * Validates the given entry. Checks for null dates, valid categories, non-negative amounts,
     * and non-null/non-empty descriptions.
     *
     * @param entry the entry to be validated
     * @throws IllegalArgumentException if the entry is not valid
     */
    private static void isValidEntry(Entry entry) {
        if (entry.getDateCreated() == null) {
            throw new IllegalArgumentException("Date of creation cannot be null");
        }
        if (entry.getDatePlanned() == null) {
            throw new IllegalArgumentException("Planned Date cannot be null");
        }
        if (entry instanceof Expense && !VALID_EXPENSE_CATEGORIES.contains(entry.getCategory())) {
            throw new IllegalArgumentException("Chosen Category is not valid for Expense");
        }
        if(entry instanceof Income && !VALID_INCOME_CATEGORIES.contains(entry.getCategory())) {
            throw new IllegalArgumentException("Chosen Category is not valid for Income");
        }
        if (entry.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (entry.getDescription() == null || entry.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
    }
}
