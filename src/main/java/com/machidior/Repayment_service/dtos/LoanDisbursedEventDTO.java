package com.machidior.Repayment_service.dtos;

import com.machidior.Repayment_service.enums.InstallmentFrequency;
import com.machidior.Repayment_service.enums.LoanProductType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanDisbursedEventDTO {
    private String loanId;
    private LoanProductType productType;
    private String customerId;
    private BigDecimal principal;
    private BigDecimal interestRate;
    private BigDecimal loanFeeRate;
    private InstallmentFrequency installmentFrequency;
    private Integer termMonths;
    private LocalDate disbursedOn;
}
