package com.machidior.Repayment_service.repo;

import com.machidior.Repayment_service.model.Repayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepaymentRepository extends JpaRepository<Repayment,Long> {
}
