
package com.banking.system.dao;

import com.banking.system.model.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface TransactionDAO {


    List<Transaction> findByAccountNumber(String accountNumber)
            throws SQLException;

    List<Transaction> findRecentTransaction(
            String accountNumber, int limit) throws SQLException;

    void save(Transaction transaction) throws SQLException;

    void save(Connection connection, Transaction transaction)
            throws SQLException;
}
