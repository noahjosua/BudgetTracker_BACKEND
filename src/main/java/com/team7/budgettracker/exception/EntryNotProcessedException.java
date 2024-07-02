package com.team7.budgettracker.exception;

public class EntryNotProcessedException extends RuntimeException {
    public EntryNotProcessedException(String message) {
        super(message);
    }
}
