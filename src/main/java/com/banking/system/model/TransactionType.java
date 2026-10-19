package com.banking.system.model;

public enum TransactionType {
    DEPOSIT,
    WITHDRAW,
    TRANSFER_OUT,
    TRANSFER_IN;

    public String displayName() {
        return name().replace('_', ' ');
    }
}
