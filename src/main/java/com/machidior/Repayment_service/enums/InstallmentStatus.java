package com.machidior.Repayment_service.enums;

import com.machidior.Repayment_service.exceptions.InvalidStatusTransitionException;

import java.util.EnumSet;
import java.util.Set;

public enum InstallmentStatus {
    PENDING,
    DUE,
    PARTIAL,
    PAID,
    OVERDUE,
    DEFAULTED;

    private Set<InstallmentStatus> nextStates;

    public boolean canTransitionTo(InstallmentStatus next) {
        if (nextStates == null) {
            return false;
        }
        return nextStates.contains(next);
    }

    public void validateTransition(InstallmentStatus next) {
        if (!canTransitionTo(next)) {
            throw new InvalidStatusTransitionException("Invalid Installment status transition: " + this + " -> " + next);
        }
    }

    static {
        PENDING.nextStates = EnumSet.of(DUE,PARTIAL, PAID, OVERDUE);
        DUE.nextStates = EnumSet.of(PARTIAL, PAID, OVERDUE);
        PARTIAL.nextStates = EnumSet.of(PAID, OVERDUE, DEFAULTED);
        OVERDUE.nextStates = EnumSet.of(DEFAULTED);
    }

}
