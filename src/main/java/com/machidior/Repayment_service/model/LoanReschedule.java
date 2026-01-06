package com.machidior.Repayment_service.model;

import com.machidior.Repayment_service.enums.RescheduleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanReschedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loanId;
    private LocalDateTime requestedDate;
    private String requestedBy;
    private String reason;
    private String newTenure;
    private String newInstallmentAmount;
    @Enumerated(EnumType.STRING)
    private RescheduleStatus status;
}
