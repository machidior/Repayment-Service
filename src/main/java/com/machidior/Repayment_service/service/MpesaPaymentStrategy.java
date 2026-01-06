package com.machidior.Repayment_service.service;

import com.machidior.Repayment_service.dtos.PaymentRequest;
import com.machidior.Repayment_service.enums.PaymentProvider;
import com.machidior.Repayment_service.model.PaymentProviderConfig;
import com.machidior.Repayment_service.repo.PaymentProviderConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MpesaPaymentStrategy implements PaymentStrategy{

    private final PaymentProviderConfigRepository paymentProviderConfigRepository;

    @Override
    public PaymentProvider getProvider() {
        return PaymentProvider.M_PESA;
    }

    @Override
    public void pay(PaymentRequest request) {

        PaymentProviderConfig config = paymentProviderConfigRepository.findByProvider(PaymentProvider.M_PESA)
                .orElseThrow(()->new IllegalStateException("MPESA not configured."));

//        ToDo: Calling MPESA configurations  and then apply payment logics
    }
}
