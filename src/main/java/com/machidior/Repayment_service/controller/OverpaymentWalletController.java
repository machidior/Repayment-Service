package com.machidior.Repayment_service.controller;

import com.machidior.Repayment_service.model.OverpaymentWallet;
import com.machidior.Repayment_service.service.OverpaymentWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/overpayment-wallet")
@RequiredArgsConstructor
public class OverpaymentWalletController {

    private final OverpaymentWalletService service;

    @GetMapping("/loan-wallet/{loanId}")
    public ResponseEntity<OverpaymentWallet> getLoanWallet(@PathVariable String loanId){
        return ResponseEntity.ok(service.getLoanWallet(loanId));
    }
}
