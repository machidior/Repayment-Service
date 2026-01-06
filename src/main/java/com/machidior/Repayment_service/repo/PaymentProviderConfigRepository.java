package com.machidior.Repayment_service.repo;

import com.machidior.Repayment_service.enums.PaymentProvider;
import com.machidior.Repayment_service.model.PaymentProviderConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentProviderConfigRepository extends JpaRepository<PaymentProviderConfig, Long> {
    Optional<PaymentProviderConfig> findByProvider(PaymentProvider provider);
}
