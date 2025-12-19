package com.machidior.Repayment_service.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExceptionResponse {

    private LocalDateTime timeStamp;
    private String exceptionMessage;
    private String exceptionDetails;
    private String exceptionCode;

    public ExceptionResponse(String exceptionMessage, String exceptionDetails, String exceptionCode){
        this.timeStamp = LocalDateTime.now();
        this.exceptionMessage = exceptionMessage;
        this.exceptionDetails = exceptionDetails;
        this.exceptionCode = exceptionCode;
    }
}
