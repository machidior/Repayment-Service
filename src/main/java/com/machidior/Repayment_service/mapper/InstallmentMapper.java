package com.machidior.Repayment_service.mapper;

import com.machidior.Repayment_service.dtos.InstallmentRequest;
import com.machidior.Repayment_service.enums.InstallmentStatus;
import com.machidior.Repayment_service.model.Installment;
import com.machidior.Repayment_service.model.LoanSchedule;
import org.springframework.stereotype.Component;

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
                .principalPaid(null)
                .interestPaid(null)
                .loanFeePaid(null)
                .totalPaid(null)
                .penaltyAccrued(null)
                .penaltyPaid(null)
                .status(InstallmentStatus.PENDING)
                .build();
    }
}
