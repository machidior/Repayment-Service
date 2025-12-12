package com.machidior.Repayment_service.model;

import com.machidior.Repayment_service.enums.LoanProductType;
import com.machidior.Repayment_service.enums.LoanScheduleStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_schedules")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loanId;
    @Enumerated(EnumType.STRING)
    private LoanProductType productType;
    private String customerId;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private BigDecimal totalPrincipal;
    private BigDecimal totalInterest;
    private BigDecimal totalLoanFees;
    private Integer totalInstallments;
    private Integer paidInstallments;
    @Enumerated(EnumType.STRING)
    private LoanScheduleStatus status;
}
