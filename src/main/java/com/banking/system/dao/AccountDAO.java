package com.banking.system.dao;

import com.banking.system.model.Account;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AccountDAO {

    void CreateAccount(Account account) throws SQLException;


    Optional<Account> findByAccountNumber(String accountNumber) throws SQLException;

    List<Account> findAllAccounts() throws SQLException;

    void updateBalance(String accountNumber, BigDecimal balance) throws SQLException;

    void updateBalance(Connection conn, String accountNumber, BigDecimal balance)
            throws SQLException;

}
