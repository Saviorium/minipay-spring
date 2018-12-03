package ru.minipay;

import ru.minipay.dao.AccountDao;
import ru.minipay.dao.AccountDaoInMemoryImpl;
import ru.minipay.service.FundTransferServiceImpl;

public class MinipayApplicationFactory {
    private static final MinipayApplicationFactory INSTANCE = new MinipayApplicationFactory();
    private MinipayApplicationFactory() {
    }

    public static MinipayApplicationFactory getInstance() {
        return INSTANCE;
    }

    public MinipayApplication createApplication() {
        AccountDao dao = new AccountDaoInMemoryImpl();
        return new MinipayApplication(dao, new FundTransferServiceImpl(dao));
    }
}
