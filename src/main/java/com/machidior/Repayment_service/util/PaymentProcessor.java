package com.machidior.Repayment_service.util;

import com.machidior.Repayment_service.dtos.PaymentRequest;
import com.machidior.Repayment_service.enums.PaymentProvider;
import com.machidior.Repayment_service.service.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PaymentProcessor {

    private final Map<PaymentProvider, PaymentStrategy> strategies;

    public PaymentProcessor(List<PaymentStrategy> strategyList) {
        strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        PaymentStrategy::getProvider,
                        Function.identity()
                ));
    }

    public void process(PaymentProvider provider, PaymentRequest request) {
        PaymentStrategy strategy = strategies.get(provider);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
        strategy.pay(request);
    }
}
