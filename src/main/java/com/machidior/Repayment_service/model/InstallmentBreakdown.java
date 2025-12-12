package com.machidior.Repayment_service.model;

import com.machidior.Repayment_service.enums.CalculationMethod;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "installment_breakdowns")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstallmentBreakdown {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long installmentId;
    private BigDecimal remainingPrincipalBefore;
    private BigDecimal remainingPrincipalAfter;
    private BigDecimal interestRatePerMonth;
    @Enumerated(EnumType.STRING)
    private CalculationMethod calculationMethod;
    private String notes;
}
