package com.example.budgettrackerv1.exception;

public class EntryNotProcessedException extends RuntimeException {
    public EntryNotProcessedException(String message) {
        super(message);
    }
}
