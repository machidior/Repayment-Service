package com.machidior.Repayment_service.repo;

import com.machidior.Repayment_service.model.PenaltyAccrual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PenaltyAccrualRepository extends JpaRepository<PenaltyAccrual, Long> {

    Optional<PenaltyAccrual> findByLoanIdAndInstallmentId(String loanId, Long installmentId);
}
