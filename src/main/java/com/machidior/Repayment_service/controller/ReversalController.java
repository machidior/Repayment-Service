package com.machidior.Repayment_service.controller;

import com.machidior.Repayment_service.model.Reversal;
import com.machidior.Repayment_service.service.ReversalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reversal")
@RequiredArgsConstructor
public class ReversalController {

    private final ReversalService service;

    @GetMapping("/repayment-id/{repaymentId}")
    public ResponseEntity<Reversal> getReversalByRepaymentId(@PathVariable Long repaymentId) {
        return ResponseEntity.ok(service.getReversalByRepaymentId(repaymentId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Reversal>> getAllReversals() {
        return ResponseEntity.ok(service.getAllReversals());
    }
}
