package com.machidior.Repayment_service.repo;

import com.machidior.Repayment_service.model.Reversal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReversalRepository extends JpaRepository<Reversal,Long> {
    Optional<Reversal> findByRepaymentId(Long repaymentId);
}
