package com.example.budgettrackerv1.exception;

import com.example.budgettrackerv1.model.ErrorResponse;
import com.google.gson.JsonSyntaxException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for handling exceptions thrown by controllers in the application.
 * This class provides centralized exception handling across all @RequestMapping methods.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link EntryNotFoundException} exceptions.
     *
     * @param ex the exception thrown when an entry is not found
     * @return a ResponseEntity containing the error response with HTTP status 404 (Not Found)
     */
    @ExceptionHandler(EntryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntryNotFoundException(EntryNotFoundException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(HttpStatus.NOT_FOUND.value());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles {@link EntryNotProcessedException} exceptions.
     *
     * @param ex the exception thrown when an entry cannot be processed
     * @return a ResponseEntity containing the error response with HTTP status 400 (Bad Request)
     */
    @ExceptionHandler(EntryNotProcessedException.class)
    public ResponseEntity<ErrorResponse> handleEntryNotProcessedException(EntryNotProcessedException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link InternalServerError} exceptions.
     *
     * @param ex the exception thrown for internal server errors
     * @return a ResponseEntity containing the error response with HTTP status 500 (Internal Server Error)
     */
    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(InternalServerError ex) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles {@link IllegalArgumentException} exceptions.
     *
     * @param ex the exception thrown for illegal arguments
     * @return a ResponseEntity containing the error response with HTTP status 400 (Bad Request)
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link JsonSyntaxException} exceptions.
     *
     * @param ignoredEx the exception thrown for JSON syntax errors
     * @return a ResponseEntity containing the error response with HTTP status 500 (Internal Server Error)
     */
    @ExceptionHandler(JsonSyntaxException.class)
    public ResponseEntity<ErrorResponse> handleJsonSyntaxException(JsonSyntaxException ignoredEx) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("Could not parse JSON.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles generic {@link Exception} exceptions.
     *
     * @param ignoredEx the exception thrown for any other unhandled errors
     * @return a ResponseEntity containing the error response with HTTP status 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ignoredEx) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("An unexpected error occurred.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
