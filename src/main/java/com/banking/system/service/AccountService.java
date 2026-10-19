package com.banking.system.service;

import com.banking.system.dao.AccountDAO;
import com.banking.system.dao.Impl.AccountDaoImpl;
import com.banking.system.model.Account;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AccountService {
    private final AccountDAO accountDao;

    public AccountService(){

        this(new AccountDaoImpl());
    }
    public AccountService(AccountDAO accountDao){

        this.accountDao = accountDao;
    }
    public Account createAccount(String accountNumber, String accountName, BigDecimal initialDeposit)
            throws SQLException {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number is required.");
        }
        if (accountName == null || accountName.trim().isEmpty()) {
            throw new IllegalArgumentException("Account name is required.");
        }
        if (initialDeposit == null || initialDeposit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial deposit cannot be null or negative.");
        }

        Optional<Account> existing = accountDao.findByAccountNumber(accountNumber.trim());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("That account number already exists.");
        }

        Account account = new Account(
                accountNumber.trim(),
                accountName.trim(),
                initialDeposit
        );
        accountDao.CreateAccount(account);
        return account;
    }
    public Optional<Account> findAccount(String accountNumber) throws SQLException {
        validateAccountNumber(accountNumber);
        return accountDao.findByAccountNumber(accountNumber.trim());
    }

    public List<Account> getAllAccounts() throws SQLException {
        return accountDao.findAllAccounts();
    }

    private void validateAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number is required.");
        }
    }
}
