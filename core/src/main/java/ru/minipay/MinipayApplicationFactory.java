package ru.minipay;

import ru.minipay.dao.AccountDao;
import ru.minipay.dao.AccountDaoInMemoryImpl;
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
        AccountDao dao = new AccountDaoInMemoryImpl();
        UserAccountsService userAccountsService = new UserAccountsService(dao);

        Map<Currency, BigDecimal> exchangeRate = new HashMap<>();
        exchangeRate.put(Currency.RUB, BigDecimal.ONE);
        exchangeRate.put(Currency.USD, new BigDecimal("65.6"));
        exchangeRate.put(Currency.EUR, new BigDecimal("74.8"));
        FundExchangeService exchangeService = new FundExchangeServiceLocalImpl(exchangeRate);

        return new MinipayApplication(userAccountsService, new FundTransferServiceImpl(dao, exchangeService));
    }
}
