package com.machidior.Repayment_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "overpayments_wallet")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OverpaymentWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerId;
    private BigDecimal balance;
    @UpdateTimestamp
    private LocalDateTime lastUpdatedDate;
}
