package com.machidior.Repayment_service.service;

import com.machidior.Repayment_service.exceptions.ResourceNotFoundException;
import com.machidior.Repayment_service.model.OverpaymentWallet;
import com.machidior.Repayment_service.repo.OverpaymentWalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OverpaymentWalletService {

    private final OverpaymentWalletRepository repository;

    @Transactional
    public void credit(String loanId, String customerId, BigDecimal amount) {

        OverpaymentWallet wallet = repository.findByCustomerIdAndLoanId(customerId,loanId)
                .orElse(new OverpaymentWallet(
                         customerId,loanId, BigDecimal.ZERO
                ));

        wallet.setBalance(wallet.getBalance().add(amount));
        repository.save(wallet);
    }

    @Transactional
    public void debit(String loanId, String customerId, BigDecimal amount) {

        OverpaymentWallet wallet = repository.findByCustomerIdAndLoanId(customerId,loanId)
                .orElseThrow(() -> new IllegalStateException("No wallet found"));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient wallet balance");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        repository.save(wallet);
    }

    public OverpaymentWallet getLoanWallet(String loanId){
        return repository.findByLoanId(loanId)
                .orElseThrow(()->new ResourceNotFoundException("No wallet for the given loan id"));
    }
}
