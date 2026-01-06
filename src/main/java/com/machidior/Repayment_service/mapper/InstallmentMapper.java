package com.machidior.Repayment_service.mapper;

import com.machidior.Repayment_service.dtos.InstallmentRequest;
import com.machidior.Repayment_service.enums.InstallmentPenaltyStatus;
import com.machidior.Repayment_service.enums.InstallmentStatus;
import com.machidior.Repayment_service.model.Installment;
import com.machidior.Repayment_service.model.LoanSchedule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class InstallmentMapper {

    public Installment toEntity(InstallmentRequest request, LoanSchedule schedule){
        return Installment.builder()
                .installmentNumber(request.getInstallmentNumber())
                .dueDate(request.getDueDate())
                .totalDue(request.getTotalDue())
                .principalDue(request.getPrincipalDue())
                .interestDue(request.getInterestDue())
                .loanFeeDue(request.getLoanFeeDue())
                .remainingBalance(request.getRemainingBalance())
                .schedule(schedule)
                .principalPaid(BigDecimal.ZERO)
                .interestPaid(BigDecimal.ZERO)
                .loanFeePaid(BigDecimal.ZERO)
                .totalPaid(BigDecimal.ZERO)
                .penaltyAccrued(BigDecimal.ZERO)
                .penaltyPaid(BigDecimal.ZERO)
                .status(InstallmentStatus.PENDING)
                .penaltyStatus(InstallmentPenaltyStatus.INACTIVE)
                .build();
    }
}
