package com.machidior.Repayment_service.controller;

import com.machidior.Repayment_service.service.InstallmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/installment")
@RequiredArgsConstructor
public class InstallmentController {

    private final InstallmentService service;

    @GetMapping("/check-penalty-activation-manually")
    public ResponseEntity<?> checkPenaltyActivation() {
        service.activateInstallmentPenaltyStatusAutomatically();
        return ResponseEntity.ok().body("Overdue installments updated successfully.");
    }
}
