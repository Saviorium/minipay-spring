package ru.minipay.model;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class AccountTest {
    private final SampleAccountGenerator accGen = SampleAccountGenerator.getInstance();

    @Test
    public void compareAccounts() {
        Account a1 = accGen.getTestAccount();
        Account a2 = accGen.getTestAccount();

        Assert.assertNotEquals(a1, a2);
        Assert.assertNotEquals(a1, new String("Test"));
        a2 = null;
        Assert.assertNotEquals(a1, a2);
        a2 = a1;
        Assert.assertEquals(a1, a2);
    }

    @Test
    public void testAccountCopyConstructor() {
        Account a1 = accGen.getTestAccount();
        a1.setBalance(100L);
        Account a2 = new Account(a1);
        a2.setBalance(200L);
        Assert.assertEquals(BigDecimal.valueOf(100L), a1.getBalance());
        Assert.assertEquals(BigDecimal.valueOf(200L), a2.getBalance());
    }
}
