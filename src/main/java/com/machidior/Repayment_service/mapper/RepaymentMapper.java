package com.machidior.Repayment_service.mapper;

import com.machidior.Repayment_service.dtos.RepaymentRequest;
import com.machidior.Repayment_service.model.Repayment;
import org.springframework.stereotype.Component;

@Component
public class RepaymentMapper {

    public Repayment toEntity(RepaymentRequest request){

        return Repayment.builder()
                .amountPaid(request.getAmountPaid())
                .paymentChannel(request.getPaymentChannel())
                .referenceNumber(request.getReferenceNumber())
                .paymentDate(request.getPaymentDate())
                .receivedAccountNumber(request.getReceivedAccountNumber())
                .build();
    }
}
