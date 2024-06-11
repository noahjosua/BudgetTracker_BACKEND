package com.example.budgettrackerv1.exception;

import com.google.gson.JsonSyntaxException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntryNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntryNotFoundException(EntryNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("code", HttpStatus.NOT_FOUND.toString());
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntryNotProcessedException.class)
    public ResponseEntity<Map<String, String>> handleEntryNotProcessedException(EntryNotProcessedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("code", HttpStatus.BAD_REQUEST.toString());
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("code", HttpStatus.BAD_REQUEST.toString());
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonSyntaxException.class)
    public ResponseEntity<Map<String, String>> handleJsonSyntaxException(JsonSyntaxException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.toString());
        response.put("message", "Could not parse JSON.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.toString());
        response.put("message", "An unexpected error occurred.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
