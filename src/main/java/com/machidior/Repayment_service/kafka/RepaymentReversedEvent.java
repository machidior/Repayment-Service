package com.machidior.Repayment_service.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RepaymentReversedEvent {
    private Long repaymentId;
    private Long installmentId;
    private BigDecimal reversedAmount;
    private LocalDateTime occurredAt;
}
