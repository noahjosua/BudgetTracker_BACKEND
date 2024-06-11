package com.example.budgettrackerv1.exception;

import com.google.gson.JsonSyntaxException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntryNotFoundException(EntryNotFoundException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(HttpStatus.NOT_FOUND.value());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntryNotProcessedException.class)
    public ResponseEntity<ErrorResponse> handleEntryNotProcessedException(EntryNotProcessedException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonSyntaxException.class)
    public ResponseEntity<ErrorResponse> handleJsonSyntaxException(JsonSyntaxException ignoredEx) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("Could not parse JSON.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ignoredEx) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("An unexpected error occurred.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
