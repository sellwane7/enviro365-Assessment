package com.enviro.assessment.junior.sellwane.model;

/**
 * Whether a withdrawal notice was successfully processed.
 * We only ever save APPROVED withdrawals to the history (a rejected one
 * throws an exception and is never persisted) - but keeping this enum
 * makes the model easy to extend later, e.g. adding a PENDING/REVERSED status.
 */
public enum WithdrawalStatus {
    APPROVED,
    REJECTED
}
