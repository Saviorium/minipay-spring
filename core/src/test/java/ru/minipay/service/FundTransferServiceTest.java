package ru.minipay.service;

import org.junit.Assert;
import org.junit.Test;
import ru.minipay.dao.AccountDao;
import ru.minipay.dao.AccountDaoInMemoryImpl;
import ru.minipay.dao.TransactionDao;
import ru.minipay.dao.TransactionDaoInMemoryImpl;
import ru.minipay.model.Account;
import ru.minipay.api.Currency;
import ru.minipay.api.FundTransferResponse;
import ru.minipay.model.SampleAccountGenerator;

import java.math.BigDecimal;

public class FundTransferServiceTest {
    private final AccountDao accountDao = new AccountDaoInMemoryImpl();
    private static final SampleAccountGenerator accGen = SampleAccountGenerator.getInstance();
    private final TransactionDao transactionDao = new TransactionDaoInMemoryImpl();
    private final FundTransferService transferService =
            new FundTransferServiceImpl(
                    accountDao, transactionDao,
                    new FundExchangeServiceLocalImpl(ExchangeRateGenerator.getSampleExchangeRate()));

    @Test
    public void checkMakeTransfer() {
        Account acc1 = accGen.getTestAccount(Currency.RUB);
        acc1.setBalance(100L);
        accountDao.insert(acc1);
        Account acc2 = accGen.getTestAccount(Currency.RUB);
        acc2.setBalance(100L);
        accountDao.insert(acc2);

        transferService.makeTransfer(acc1.getId(), acc2.getId(), Currency.RUB, BigDecimal.valueOf(50L));

        acc1 = accountDao.getById(acc1.getId());
        acc2 = accountDao.getById(acc2.getId());
        Assert.assertEquals(0, BigDecimal.valueOf(50L).compareTo(acc1.getBalance()));
        Assert.assertEquals(0, BigDecimal.valueOf(150L).compareTo(acc2.getBalance()));
    }

    @Test
    public void makeTransferToUnknownAccount() {
        Account acc1 = accGen.getTestAccount(Currency.RUB);
        acc1.setBalance(100L);
        accountDao.insert(acc1);
        Account acc2 = accGen.getTestAccount(Currency.RUB);
        acc2.setBalance(100L);

        FundTransferResponse result = transferService.makeTransfer(acc1.getId(), acc2.getId(), Currency.RUB, BigDecimal.valueOf(50L));
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void transferWhenNotEnoughMoney() {
        Account acc1 = accGen.getTestAccount(Currency.RUB);
        acc1.setBalance(100L);
        accountDao.insert(acc1);
        Account acc2 = accGen.getTestAccount(Currency.RUB);
        acc2.setBalance(100L);
        accountDao.insert(acc2);

        FundTransferResponse result = transferService.makeTransfer(acc1.getId(), acc2.getId(), Currency.RUB, BigDecimal.valueOf(101L));
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals("Check balance unchanged in accountDao",
                BigDecimal.valueOf(100L), accountDao.getById(acc1.getId()).getBalance());
        Assert.assertEquals("Check balance unchanged in accountDao",
                BigDecimal.valueOf(100L), accountDao.getById(acc2.getId()).getBalance());
    }
}
