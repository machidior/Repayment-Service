package com.machidior.Repayment_service.model;

import com.machidior.Repayment_service.enums.PaymentProvider;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentProviderConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentProvider provider;

    private String accountNumber;

    private String apiEndpoint;

    private Boolean active;

    @CreatedDate
    private LocalDateTime createdAt;
}
