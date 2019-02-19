package ru.minipay;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.minipay.dao.*;
import ru.minipay.api.Currency;
import ru.minipay.service.FundExchangeService;
import ru.minipay.service.FundExchangeServiceLocalImpl;
import ru.minipay.service.FundTransferServiceImpl;
import ru.minipay.service.UserAccountsService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MinipayApplicationFactory {
    private static final MinipayApplicationFactory INSTANCE = new MinipayApplicationFactory();
    private MinipayApplicationFactory() {
    }

    public static MinipayApplicationFactory getInstance() {
        return INSTANCE;
    }

    public MinipayApplication createApplication() {
        AccountDao accountDao = new AccountDaoInMemoryImpl();
        TransactionDao transactionDao = new TransactionDaoInMemoryImpl();
        UserAccountsService userAccountsService = new UserAccountsService(accountDao);

        FundExchangeService exchangeService = getExchangeService();

        return new MinipayApplication(userAccountsService, new FundTransferServiceImpl(accountDao, transactionDao, exchangeService));
    }

    public MinipayApplication createPostgresApplication() {
        BasicDataSource ds = getPostgresDataSource();

        AccountDao accountDao = new AccountDaoDbImpl(ds);
        UserAccountsService userAccountsService = new UserAccountsService(accountDao);

        TransactionDao transactionDao = new TransactionDaoDbImpl(ds);

        FundExchangeService exchangeService = getExchangeService();

        return new MinipayApplication(userAccountsService, new FundTransferServiceImpl(accountDao, transactionDao, exchangeService));
    }

    private BasicDataSource getPostgresDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost/minipay");
        ds.setUsername("minipay");
        ds.setPassword("sVWneZ4eA");
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
        return ds;
    }

    private FundExchangeService getExchangeService() {
        Map<Currency, BigDecimal> exchangeRate = new HashMap<>();
        exchangeRate.put(Currency.RUB, BigDecimal.ONE);
        exchangeRate.put(Currency.USD, new BigDecimal("65.6"));
        exchangeRate.put(Currency.EUR, new BigDecimal("74.8"));
        return new FundExchangeServiceLocalImpl(exchangeRate);
    }
}
