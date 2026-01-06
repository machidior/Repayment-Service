package com.machidior.Repayment_service.exceptions;

public class AlreadyReversedException extends RuntimeException {
    public AlreadyReversedException(String message) {
        super(message);
    }
}
