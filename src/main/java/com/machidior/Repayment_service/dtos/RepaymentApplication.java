package com.machidior.Repayment_service.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepaymentApplication {
    private Long repaymentId;
    private Long installmentId;
    private BigDecimal principalApplied;
    private BigDecimal interestApplied;
    private BigDecimal loanFeeApplied;
    private BigDecimal penaltyApplied;
    private LocalDateTime appliedTimestamp;
}
