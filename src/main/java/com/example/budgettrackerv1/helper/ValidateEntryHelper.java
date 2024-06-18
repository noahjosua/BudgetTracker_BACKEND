package com.example.budgettrackerv1.helper;

import com.example.budgettrackerv1.Constants;
import com.example.budgettrackerv1.model.Category;
import com.example.budgettrackerv1.model.Entry;
import com.example.budgettrackerv1.model.Expense;
import com.example.budgettrackerv1.model.Income;

import java.util.*;

public class ValidateEntryHelper {

    private static final List<Category> VALID_INCOME_CATEGORIES = Arrays.asList(
            Category.SALARY, Category.POCKET_MONEY, Category.ALIMENT, Category.CAPITAL_ASSETS, Category.RENTAL
    );

    private static final List<Category> VALID_EXPENSE_CATEGORIES = Arrays.asList(
            Category.GROCERIES, Category.DRUGSTORE, Category.FREE_TIME,
            Category.RENT, Category.INSURANCE, Category.SUBSCRIPTIONS,
            Category.EDUCATION, Category.OTHER
    );

    public static Map<String, Object> buildResponseBody(String message, Object entry) {
        return Map.of(
                Constants.RESPONSE_MESSAGE_KEY, message,
                Constants.RESPONSE_ENTRY_KEY, entry
        );
    }

    public static void isValid(Entry entry) {
        isValidEntry(entry);
    }

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
