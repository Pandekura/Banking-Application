package com.banking.system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Transaction {
    private String transactionId;
    private String accountNumber;
    private TransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String referenceNumber;
    private String remarks;
    private LocalDateTime createdAt;

    public Transaction(String accountNumber, TransactionType transactionType,
                       BigDecimal amount, BigDecimal balanceAfter,
                       String referenceNumber, String remarks) {
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.referenceNumber = referenceNumber;
        this.remarks = remarks;

    }
}
