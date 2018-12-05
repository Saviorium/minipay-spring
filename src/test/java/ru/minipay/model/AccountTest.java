package ru.minipay.model;

import org.junit.Assert;
import org.junit.Test;

public class AccountTest {
    SampleAccountGenerator accGen = SampleAccountGenerator.getInstance();

    @Test
    public void compareAccounts() {
        Account a1 = accGen.getTestAccount();
        Account a2 = accGen.getTestAccount();

        Assert.assertFalse(a1.equals(a2));
        Assert.assertFalse(a1.equals(new String("Test")));
        a2 = null;
        Assert.assertFalse(a1.equals(a2));
        a2 = a1;
        Assert.assertTrue(a1.equals(a2));
    }
}
