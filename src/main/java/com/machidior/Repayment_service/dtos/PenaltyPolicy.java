package com.machidior.Repayment_service.dtos;

import com.machidior.grpc.loanconfig.LoanProductType;
import com.machidior.grpc.loanconfig.PenaltyCalculationType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PenaltyPolicy {
    private LoanProductType productType;
    private BigDecimal latePenaltyRate;
    private Integer gracePeriodDays;
    private PenaltyCalculationType calculationType;

}
