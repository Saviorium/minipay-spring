package ru.minipay;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.minipay.exceptions.DataAccessException;
import ru.minipay.model.Account;
import ru.minipay.api.Currency;
import ru.minipay.model.SampleAccountGenerator;
import ru.minipay.api.FundTransferResponse;

import java.math.BigDecimal;

public class MinipayApplicationTest {
    private static MinipayApplication app;
    private final SampleAccountGenerator accGen = SampleAccountGenerator.getInstance();

    @BeforeClass
    public static void setUp() {
        MinipayApplicationFactory appFactory = MinipayApplicationFactory.getInstance();
        app = appFactory.createApplication();
    }

    @Test
    public void checkCreateAccount() throws DataAccessException {
        BigDecimal balance = new BigDecimal("100.01");
        Account acc = app.createAccount(accGen.getTestUser(), Currency.RUB, balance);
        Assert.assertNotNull(acc.getId());
        Assert.assertEquals(0, balance.compareTo(acc.getBalance()));
    }

    @Test
    public void checkMakeTransfer() throws DataAccessException {
        BigDecimal balance = new BigDecimal("999.99");
        Account acc1 = app.createAccount(accGen.getTestUser(), Currency.RUB, balance);
        Account acc2 = app.createAccount(accGen.getTestUser(), Currency.RUB, balance);

        FundTransferResponse result = app.makeTransfer(acc1.getId(), acc2.getId(), Currency.RUB, BigDecimal.valueOf(50L));

        Assert.assertTrue("Check result is success", result.isSuccess());
    }
}
