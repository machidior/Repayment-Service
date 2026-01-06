package com.machidior.Repayment_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "arrears_record")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArrearsRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loanId;
    private Long installmentId;
    private Integer daysOverdue;
    private BigDecimal penaltyAccrued;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
