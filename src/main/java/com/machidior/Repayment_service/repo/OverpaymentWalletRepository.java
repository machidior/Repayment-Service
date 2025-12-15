package com.machidior.Repayment_service.repo;

import com.machidior.Repayment_service.model.OverpaymentWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OverpaymentWalletRepository extends JpaRepository<OverpaymentWallet, Long> {
    List<OverpaymentWallet> findByCustomerId(String customerId);
    Optional<OverpaymentWallet> findByCustomerIdAndLoanId(String customerId, String loanId);
    Optional<OverpaymentWallet> findByLoanId(String loanId);
}
