package com.example.budgettrackerv1.exception;

public class EntryNotFoundException extends RuntimeException {

    public EntryNotFoundException(String message) {
        super(message);
    }
}
