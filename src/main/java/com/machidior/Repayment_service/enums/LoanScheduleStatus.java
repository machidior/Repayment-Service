package com.machidior.Repayment_service.enums;

import com.machidior.Repayment_service.exceptions.InvalidStatusTransitionException;

import java.util.EnumSet;
import java.util.Set;

public enum LoanScheduleStatus {
    PENDING,
    ACTIVE,
    COMPLETED,
    CANCELLED;

    private Set<LoanScheduleStatus> allowedTransitions;

    public boolean canTransitionTo(LoanScheduleStatus next) {
        if(allowedTransitions == null) {
            return false;
        }
        return allowedTransitions.contains(next);
    }

    public void validateTransition(LoanScheduleStatus next) {
        if (!canTransitionTo(next)) {
            throw new InvalidStatusTransitionException("Invalid payment status transition: " + this + " -> " + next);
        }
    }

    static {

        PENDING.allowedTransitions = EnumSet.of(ACTIVE, CANCELLED);
        ACTIVE.allowedTransitions = EnumSet.of(COMPLETED, CANCELLED);
    }

}
