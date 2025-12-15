package com.machidior.Repayment_service.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RepaymentAppliedEvent {
    private Long repaymentId;
    private String loanId;
    private Long installmentId;
    private String customerId;
    private BigDecimal amountApplied;
    private BigDecimal excessAmount;
    private LocalDateTime occurredAt;
}
