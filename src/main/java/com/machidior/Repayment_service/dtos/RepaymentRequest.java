package com.machidior.Repayment_service.dtos;

import com.machidior.Repayment_service.enums.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepaymentRequest {

    private BigDecimal amountPaid;
    private PaymentMethod paymentChannel;
    private String referenceNumber;
    private LocalDate paymentDate;
    private String receivedAccountNumber;
}
