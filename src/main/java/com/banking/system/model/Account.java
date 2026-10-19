package com.banking.system.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class Account {
    private Long id;
    private String accountNumber;
    private String accountName;
    private BigDecimal balance;
    private LocalDateTime createdAt;


    public Account(String accountNumber, String accountName, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.balance = balance;
    }
}
