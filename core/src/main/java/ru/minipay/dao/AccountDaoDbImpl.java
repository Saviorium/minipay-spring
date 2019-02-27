package ru.minipay.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.minipay.api.Currency;
import ru.minipay.model.Account;
import ru.minipay.model.Gender;
import ru.minipay.model.User;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class AccountDaoDbImpl implements AccountDao {
    private final BasicDataSource ds;

    public AccountDaoDbImpl(BasicDataSource ds) {
        this.ds = ds;
    }

    @Override
    public void insert(Account account) {
        try (Connection conn = ds.getConnection()){
            insertSingleAccount(conn, account);
        } catch (SQLException e) {
            e.printStackTrace(); // todo: throw custom exception
            System.exit(1);
        }
    }

    @Override
    public void insert(List<Account> accounts) {
        try (Connection conn = ds.getConnection()){
            conn.setAutoCommit(false);
            try {
                for (Account acc : accounts) {
                    insertSingleAccount(conn, acc);
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace(); // todo: throw custom exception
            System.exit(1);
        }
    }

    private void insertSingleAccount(Connection conn, Account account) throws SQLException {
        String query = "SELECT COUNT(*) FROM accounts WHERE id = ?;";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setObject(1, account.getId());
        ResultSet result = statement.executeQuery();
        result.next();
        boolean accountExists = result.getInt(1) == 1;
        if(accountExists) {
            query = "UPDATE public.accounts " +
                    "SET id=?, currency=?, created=?, last_changed=?, balance=?, first_name=?, last_name=?, gender=?, birthday=? " +
                    "WHERE id=?;";
        } else {
            query = "INSERT INTO accounts(" +
                    "id, currency, created, last_changed, balance, first_name, last_name, gender, birthday) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        }
        statement = conn.prepareStatement(query);
        statement.setObject(1, account.getId());
        statement.setString(2, account.getCurrency().toString());
        statement.setObject(3, Timestamp.from(account.getCreated()));
        statement.setLong(4, account.getLastChanged());
        statement.setObject(5, account.getBalance());
        statement.setString(6, account.getUser().getFirstName());
        statement.setString(7, account.getUser().getLastName());
        statement.setInt(8, account.getUser().getGender().getId());
        statement.setObject(9, account.getUser().getBirthday());
        if(accountExists) {
            statement.setObject(10, account.getId());
        }
        statement.execute();
    }

    @Override
    public Account getById(UUID id) {
        try (Connection conn = ds.getConnection()){
            String query = "SELECT * FROM accounts WHERE id=?;";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setObject(1, id);
            ResultSet result = statement.executeQuery();
            if(result.next()) {
                User user = new User(
                        result.getString(6),
                        result.getString(7),
                        Gender.getById(result.getInt(8)),
                        result.getDate(9).toLocalDate()
                );
                return new Account(id, Currency.valueOf(result.getString(2)), result.getTimestamp(3).toInstant(),
                        result.getLong(4), user, result.getBigDecimal(5));
            } else return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
