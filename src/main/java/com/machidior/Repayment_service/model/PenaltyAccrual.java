package com.machidior.Repayment_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PenaltyAccrual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loanId;
    private Long installmentId;
    @CreatedDate
    private LocalDateTime accruedDate;
    private BigDecimal penaltyAmount;
    private BigDecimal appliedToInstallment;
}
