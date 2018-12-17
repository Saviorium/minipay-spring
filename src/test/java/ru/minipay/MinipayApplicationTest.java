package ru.minipay;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.minipay.model.Account;
import ru.minipay.model.Currency;
import ru.minipay.model.SampleAccountGenerator;

import java.math.BigDecimal;

public class MinipayApplicationTest {
    private MinipayApplication app;
    private final SampleAccountGenerator accGen = SampleAccountGenerator.getInstance();

    @BeforeClass
    public void setUp() {
        MinipayApplicationFactory appFactory = MinipayApplicationFactory.getInstance();
        app = appFactory.createApplication();
    }

    @Test
    public void checkCreateAccount() {
        BigDecimal balance = new BigDecimal("100.01");
        Account acc = app.createAccount(SampleAccountGenerator.getTestUser(), Currency.RUB, balance);
        Assert.assertNotNull(acc.getId());
        Assert.assertEquals(0, balance.compareTo(acc.getBalance()));
    }
}
