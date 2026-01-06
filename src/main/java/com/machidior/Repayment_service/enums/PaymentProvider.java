package com.machidior.Repayment_service.enums;


public enum PaymentProvider {

    M_PESA(PaymentMethod.MOBILE_MONEY),
    MIX_BY_YAS(PaymentMethod.MOBILE_MONEY),
    HALO_PESA(PaymentMethod.MOBILE_MONEY),
    AIRTEL_MONEY(PaymentMethod.MOBILE_MONEY),

    NMB(PaymentMethod.BANK_TRANSFER),
    CRDB(PaymentMethod.BANK_TRANSFER),
    NBC(PaymentMethod.BANK_TRANSFER);

    private final PaymentMethod paymentMethod;

    PaymentProvider(PaymentMethod method) {
        this.paymentMethod = method;
    }
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

}