package com.machidior.Repayment_service.enums;

import com.machidior.Repayment_service.exceptions.InvalidStatusTransitionException;

import java.util.EnumSet;
import java.util.Set;

public enum RepaymentStatus {
    PENDING,
    PARTIAL,
    OVERPAID,
    FAILED,
    APPLIED,
    REVERSED,
    REFUNDED;

    private Set<RepaymentStatus> allowedTransitions;


    public boolean canTransitionTo(RepaymentStatus next) {
        if(allowedTransitions == null) {
            return false;
        }
        return allowedTransitions.contains(next);
    }

    public void validateTransition(RepaymentStatus next) {
        if (!canTransitionTo(next)) {
            throw new InvalidStatusTransitionException("Invalid payment status transition: " + this + " -> " + next);
        }
    }

    static {
        PENDING.allowedTransitions = EnumSet.of(PARTIAL, OVERPAID, FAILED, APPLIED, REVERSED, REFUNDED);
        PARTIAL.allowedTransitions = EnumSet.of(OVERPAID, APPLIED, REVERSED, REFUNDED, FAILED);
        OVERPAID.allowedTransitions = EnumSet.of(REVERSED, REFUNDED, FAILED);
        APPLIED.allowedTransitions = EnumSet.of(FAILED, REVERSED, REFUNDED);
    }
}
