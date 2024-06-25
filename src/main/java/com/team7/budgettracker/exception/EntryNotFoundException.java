package com.team7.budgettracker.exception;

public class EntryNotFoundException extends RuntimeException {

    public EntryNotFoundException(String message) {
        super(message);
    }
}
