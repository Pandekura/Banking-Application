
package com.banking.system.dao.Impl;

import com.banking.system.dao.TransactionDAO;
import com.banking.system.database.DatabaseConnection;
import com.banking.system.model.Transaction;
import com.banking.system.model.TransactionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDaoImpl implements TransactionDAO {

    private static final String INSERT_TRANSACTION =
            "INSERT INTO transactions " +
                    "(transaction_reference, account_number, transaction_type, amount, balance) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String FIND_BY_ACCOUNT_NUMBER =
            "SELECT id, account_number, transaction_type, amount, balance, " +
                    "transaction_reference, remarks, created_at " +
                    "FROM transactions WHERE account_number = ? " +
                    "ORDER BY created_at DESC";

    private static final String FIND_RECENT_TRANSACTIONS =
            "SELECT id, account_number, transaction_type, amount, balance, " +
                    "transaction_reference, remarks, created_at " +
                    "FROM transactions WHERE account_number = ? " +
                    "ORDER BY created_at DESC LIMIT ?";

    @Override
    public List<Transaction> findByAccountNumber(String accountNumber)
            throws SQLException {

        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps =
                     connection.prepareStatement(FIND_BY_ACCOUNT_NUMBER)) {

            ps.setString(1, accountNumber);

            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    transactions.add(mapRow(result));
                }
            }
        }

        return transactions;
    }

    @Override
    public List<Transaction> findRecentTransaction(
            String accountNumber, int limit) throws SQLException {

        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps =
                     connection.prepareStatement(FIND_RECENT_TRANSACTIONS)) {

            ps.setString(1, accountNumber);
            ps.setInt(2, limit);

            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    transactions.add(mapRow(result));
                }
            }
        }

        return transactions;
    }

    @Override
    public void save(Transaction transaction) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            save(connection, transaction);
        }
    }

    @Override
    public void save(Connection connection, Transaction transaction)
            throws SQLException {

        try (PreparedStatement ps = connection.prepareStatement(
                INSERT_TRANSACTION, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, transaction.getReferenceNumber());
            ps.setString(2, transaction.getAccountNumber());
            ps.setString(3, transaction.getTransactionType().name());
            ps.setBigDecimal(4, transaction.getAmount());
            ps.setBigDecimal(5, transaction.getBalanceAfter());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    transaction.setTransactionId(
                            String.valueOf(keys.getLong(1)));
                }
            }
        }
    }

    private Transaction mapRow(ResultSet result) throws SQLException {
        Transaction transaction = new Transaction();

        transaction.setTransactionId(
                String.valueOf(result.getLong("id")));

        transaction.setAccountNumber(
                result.getString("account_number"));

        transaction.setTransactionType(
                TransactionType.valueOf(
                        result.getString("transaction_type")));

        transaction.setAmount(result.getBigDecimal("amount"));
        transaction.setBalanceAfter(result.getBigDecimal("balance"));
        transaction.setReferenceNumber(
                result.getString("transaction_reference"));

        transaction.setRemarks(result.getString("remarks"));

        Timestamp createdAt = result.getTimestamp("created_at");

        if (createdAt != null) {
            transaction.setCreatedAt(createdAt.toLocalDateTime());
        }

        return transaction;
    }
}
