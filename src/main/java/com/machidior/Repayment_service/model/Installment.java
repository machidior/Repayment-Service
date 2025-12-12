package com.machidior.Repayment_service.model;

import com.machidior.Repayment_service.enums.InstallmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "installments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Installment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long scheduleId;
    private Integer installmentNumber;
    private LocalDate dueDate;
    private BigDecimal principalDue;
    private BigDecimal interestDue;
    private BigDecimal loanFeeDue;
    private BigDecimal totalDue;

    private BigDecimal principalPaid;
    private BigDecimal interestPaid;
    private BigDecimal loanFeePaid;
    private BigDecimal totalPaid;
    private BigDecimal remainingBalance;

    private BigDecimal penaltyAccrued;
    private BigDecimal penaltyPaid;
    @Enumerated(EnumType.STRING)
    private InstallmentStatus status;
}
