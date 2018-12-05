package ru.minipay.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.minipay.dao.AccountDao;
import ru.minipay.dao.AccountDaoInMemoryImpl;
import ru.minipay.model.Account;
import ru.minipay.model.Currency;
import ru.minipay.model.SampleAccountGenerator;

import java.math.BigDecimal;

public class FundTransferServiceTest {
    private final AccountDao dao = new AccountDaoInMemoryImpl();
    private final FundTransferService transferService =
            new FundTransferServiceImpl(
                    dao,
                    new FundExchangeServiceLocalImpl(ExchangeRateGenerator.getSampleExchangeRate()));

    @Before
    public void setUp() {

    }

    @Test
    public void checkMakeTransfer() {
        Account acc1 = SampleAccountGenerator.getTestAccount(Currency.RUB);
        acc1.setBalance(100L);
        dao.insert(acc1);
        Account acc2 = SampleAccountGenerator.getTestAccount(Currency.RUB);
        acc2.setBalance(100L);
        dao.insert(acc2);

        transferService.makeTransfer(acc1.getId(), acc2.getId(), Currency.RUB, BigDecimal.valueOf(50L));

        acc1 = dao.getById(acc1.getId());
        acc2 = dao.getById(acc2.getId());
        Assert.assertEquals(0, BigDecimal.valueOf(50L).compareTo(acc1.getBalance()));
        Assert.assertEquals(0, BigDecimal.valueOf(150L).compareTo(acc2.getBalance()));
    }

    @Test
    public void makeTransferToUnknownAccount() {
        Account acc1 = SampleAccountGenerator.getTestAccount(Currency.RUB);
        acc1.setBalance(100L);
        dao.insert(acc1);
        Account acc2 = SampleAccountGenerator.getTestAccount(Currency.RUB);
        acc2.setBalance(100L);

        //NullPointerException
        //transferService.makeTransfer(acc1.getId(), acc2.getId(), Currency.RUB, BigDecimal.valueOf(50L));
    }
}
