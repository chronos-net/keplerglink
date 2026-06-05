package com.edomex.kiliantRSP.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        for (FieldError err : ex.getBindingResult().getFieldErrors()) {
            errors.put(err.getField(), err.getDefaultMessage());
        }

        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(ConstraintViolationException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(cv ->
                errors.put(cv.getPropertyPath().toString(), cv.getMessage())
        );

        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Map<String,String>> handleTransactionSystem(TransactionSystemException ex) {
        //obtenemos la causa del error
        Throwable root = NestedExceptionUtils.getRootCause(ex);
        String message = (root != null ? root.getMessage() : ex.getMessage());

        Map<String, String> body = new HashMap<>();
        body.put("message", ex.getMessage());

        return  ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }

    @ExceptionHandler (Exception.class)
    public ResponseEntity<Map<String,String>> handleException(Exception ex){
        Map<String,String> body = new HashMap<>();
        body.put("message", ex.getMessage());

        return  ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }

}
