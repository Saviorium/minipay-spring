package ru.minipay.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.minipay.api.Currency;
import ru.minipay.model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionDaoDbImpl implements TransactionDao {
    private final BasicDataSource ds;

    public TransactionDaoDbImpl(BasicDataSource ds) {
        this.ds = ds;
    }

    @Override
    public void insert(Transaction txn) {
        try (Connection conn = ds.getConnection()) {
            String query = "INSERT INTO transactions(id_from, id_to, currency, amount, \"timestamp\") VALUES (?, ?, ?, ?, ?);";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setObject(1, txn.getFromId());
            statement.setObject(2, txn.getToId());
            statement.setString(3, txn.getCurrency().toString());
            statement.setObject(4, txn.getAmount());
            statement.setObject(5, Timestamp.from(txn.getTimestamp()));
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public List<Transaction> getAllById(UUID id) {
        List<Transaction> txnFound = new ArrayList<>();

        try (Connection conn = ds.getConnection()) {
            String query = "SELECT * FROM transactions WHERE id_from = ? OR id_to = ?;";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setObject(1, id);
            statement.setObject(2, id);
            ResultSet results = statement.executeQuery();

            while(results.next()) {
                Transaction txn = new Transaction(
                        results.getObject(1, UUID.class),
                        results.getObject(2, UUID.class),
                        Currency.RUB, // FIXME: hardcoded currency
                        results.getBigDecimal(4),
                        results.getTimestamp(5).toInstant()
                );
                txnFound.add(txn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return txnFound;
    }
}
