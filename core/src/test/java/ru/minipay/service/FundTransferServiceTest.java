package ru.minipay.service;

import org.junit.Assert;
import org.junit.Test;
import ru.minipay.dao.AccountDao;
import ru.minipay.dao.AccountDaoInMemoryImpl;
import ru.minipay.model.Account;
import ru.minipay.api.Currency;
import ru.minipay.api.FundTransferResult;
import ru.minipay.model.SampleAccountGenerator;

import java.math.BigDecimal;

public class FundTransferServiceTest {
    private final AccountDao dao = new AccountDaoInMemoryImpl();
    private static final SampleAccountGenerator accGen = SampleAccountGenerator.getInstance();
    private final FundTransferService transferService =
            new FundTransferServiceImpl(
                    dao,
                    new FundExchangeServiceLocalImpl(ExchangeRateGenerator.getSampleExchangeRate()));

    @Test
    public void checkMakeTransfer() {
        Account acc1 = accGen.getTestAccount(Currency.RUB);
        acc1.setBalance(100L);
        dao.insert(acc1);
        Account acc2 = accGen.getTestAccount(Currency.RUB);
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
        Account acc1 = accGen.getTestAccount(Currency.RUB);
        acc1.setBalance(100L);
        dao.insert(acc1);
        Account acc2 = accGen.getTestAccount(Currency.RUB);
        acc2.setBalance(100L);

        FundTransferResult result = transferService.makeTransfer(acc1.getId(), acc2.getId(), Currency.RUB, BigDecimal.valueOf(50L));
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void transferWhenNotEnoughMoney() {
        Account acc1 = accGen.getTestAccount(Currency.RUB);
        acc1.setBalance(100L);
        dao.insert(acc1);
        Account acc2 = accGen.getTestAccount(Currency.RUB);
        acc2.setBalance(100L);
        dao.insert(acc2);

        FundTransferResult result = transferService.makeTransfer(acc1.getId(), acc2.getId(), Currency.RUB, BigDecimal.valueOf(101L));
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals("Check balance unchanged in dao",
                BigDecimal.valueOf(100L), dao.getById(acc1.getId()).getBalance());
        Assert.assertEquals("Check balance unchanged in dao",
                BigDecimal.valueOf(100L), dao.getById(acc2.getId()).getBalance());
    }
}
