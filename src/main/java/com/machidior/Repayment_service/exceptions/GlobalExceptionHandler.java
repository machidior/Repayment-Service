package com.machidior.Repayment_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<?> handleOptimisticLocking(
            ObjectOptimisticLockingFailureException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("The record was updated by another transaction. Please retry.");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(
            ResourceNotFoundException e,
            WebRequest webRequest
    ){
        ExceptionResponse response = new ExceptionResponse(
                e.getMessage(),
                webRequest.getDescription(false),
                "RESOURCE NOT FOUND"
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
