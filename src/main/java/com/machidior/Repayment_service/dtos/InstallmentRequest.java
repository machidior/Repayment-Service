package com.machidior.Repayment_service.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstallmentRequest {

    private int installmentNumber;
    private LocalDate dueDate;
    private BigDecimal totalDue;
    private BigDecimal principalDue;
    private BigDecimal interestDue;
    private BigDecimal loanFeeDue;
    private BigDecimal remainingBalance;
}
