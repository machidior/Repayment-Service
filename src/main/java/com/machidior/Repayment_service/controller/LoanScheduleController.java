package com.machidior.Repayment_service.controller;

import com.machidior.Repayment_service.enums.LoanProductType;
import com.machidior.Repayment_service.model.LoanSchedule;
import com.machidior.Repayment_service.service.LoanScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loan-schedules")
@RequiredArgsConstructor
public class LoanScheduleController {

    private final LoanScheduleService service;

    @GetMapping("/{id}")
    public ResponseEntity<LoanSchedule> getLoanSchedule(@PathVariable Long id){
        return ResponseEntity.ok(service.getLoanSchedule(id));
    }

    @GetMapping("/loan-id/{loanId}")
    public ResponseEntity<LoanSchedule> getLoanScheduleByLoanId(@PathVariable String loanId){
        return ResponseEntity.ok(service.getLoanScheduleByLoanId(loanId));
    }

    @GetMapping("/product-type/{productType}")
    public ResponseEntity<List<LoanSchedule>> getLoanSchedulesByProductType(@PathVariable LoanProductType productType){
        return ResponseEntity.ok(service.getLoanScheduleByProductType(productType));
    }
}
