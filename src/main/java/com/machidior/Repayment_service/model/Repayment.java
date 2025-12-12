package com.machidior.Repayment_service.model;

import com.machidior.Repayment_service.enums.Currency;
import com.machidior.Repayment_service.enums.PaymentMethod;
import com.machidior.Repayment_service.enums.RepaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "repayments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Repayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loanId;
    private String customerId;
    private BigDecimal amountPaid;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentChannel;
    private String referenceNumber;
    private LocalDate paymentDate;
    private String receivedByUserId;
    @Enumerated(EnumType.STRING)
    private RepaymentStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
