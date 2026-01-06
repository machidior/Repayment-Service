package com.machidior.Repayment_service.service;

import com.machidior.Repayment_service.dtos.PaymentRequest;
import com.machidior.Repayment_service.enums.PaymentProvider;

public interface PaymentStrategy {

    PaymentProvider getProvider();

    void pay(PaymentRequest request);
}
