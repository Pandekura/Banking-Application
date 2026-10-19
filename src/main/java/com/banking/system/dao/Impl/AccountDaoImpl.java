package com.banking.system.dao.Impl;

import com.banking.system.dao.AccountDAO;
import com.banking.system.database.DatabaseConnection;
import com.banking.system.model.Account;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDaoImpl implements AccountDAO {

    private static final String INSERT_ACCOUNT =
            "INSERT INTO accounts (account_number, account_name, balance)VALUES (?, ?, ?)";

    private static final String FIND_BY_ACCOUNT_NUMBER =
            "SELECT id, account_number, account_name, balance, created_at "+
                    "FROM accounts WHERE account_number = ?";
    private static final String FIND_ALL =
            "SELECT id, account_number, account_name, balance, created_at " +
                    "FROM accounts ORDER BY id";
    private static final String UPDATE_BALANCE =
            "UPDATE accounts SET balance = ? WHERE account_number = ?";



    @Override
    public void CreateAccount(Account account) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ACCOUNT, Statement.RETURN_GENERATED_KEYS)) {


            preparedStatement.setString(1, account.getAccountNumber());
            preparedStatement.setString(2, account.getAccountName());
            preparedStatement.setBigDecimal(3, account.getBalance());
            preparedStatement.executeUpdate();

            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    account.setId(keys.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create account, Please try again.", e);
        }
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ACCOUNT_NUMBER);
        {

            preparedStatement.setString(1, accountNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            } catch (SQLException e) {
                throw new RuntimeException("Error cannot find account", e);
            }

        }

    }

    @Override
    public List<Account> findAllAccounts()throws SQLException {
        List<Account> accounts = new ArrayList<>();
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL);
            ResultSet result = preparedStatement.executeQuery()){
            while (result.next()) {
                accounts.add(mapRow(result));
            }
            return accounts;
        }catch(SQLException e){
            throw new RuntimeException("Failed to list all accounts", e);
        }
    }
    @Override
    public void updateBalance(String accountNumber, BigDecimal balance) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            updateBalance(connection, accountNumber, balance);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update balance",e);
        }
    }

    @Override
    public void updateBalance(Connection connection, String accountNumber, BigDecimal balance)
            throws SQLException {

        try (PreparedStatement ps = connection.prepareStatement(UPDATE_BALANCE)) {
            ps.setBigDecimal(1, balance);
            ps.setString(2, accountNumber);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Balance update affected 0 rows for account: " + accountNumber);
            }
        }
    }

    private Account mapRow (ResultSet result) throws SQLException {
        return new Account(
                result.getString("account_number"),
                result.getString("account_name"),
                result.getBigDecimal("balance")
        );

    }

}
