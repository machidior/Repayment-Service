package com.machidior.Repayment_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepaymentApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal principalApplied;
    private BigDecimal interestApplied;
    private BigDecimal loanFeeApplied;
    private BigDecimal penaltyApplied;
    private BigDecimal totalApplied;
    private BigDecimal excessAmount;
    @CreationTimestamp
    private LocalDateTime appliedTimestamp;
}
