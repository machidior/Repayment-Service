package com.machidior.Repayment_service.repo;

import com.machidior.Repayment_service.enums.LoanProductType;
import com.machidior.Repayment_service.model.LoanSchedule;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanScheduleRepository extends JpaRepository<LoanSchedule,Long> {
    Optional<LoanSchedule> findByLoanId(String loanId);
    List<LoanSchedule> findByProductType(LoanProductType productType);
}
