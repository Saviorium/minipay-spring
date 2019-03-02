package ru.minipay.dao;

import org.junit.Assert;
import org.junit.Test;
import ru.minipay.exceptions.DataAccessException;
import ru.minipay.model.Account;
import ru.minipay.model.SampleAccountGenerator;

import java.util.LinkedList;

public class AccountDaoInMemoryImplTest {
    private AccountDao accountDao = new AccountDaoInMemoryImpl();

    private static final SampleAccountGenerator accGen = SampleAccountGenerator.getInstance();

    @Test
    public void testInMemMultipleInsert() throws DataAccessException {
        LinkedList<Account> accs = new LinkedList<>();
        final int accNum = 3;
        for (int i = 0; i < accNum; i++) {
            accs.add(accGen.getTestAccount());
        }
        accountDao.insert(accs);
        while (!accs.isEmpty()) {
            Account acc = accs.pop();
            Account daoAcc = accountDao.getById(acc.getId());
            Assert.assertEquals(acc.getBalance(), daoAcc.getBalance());
            Assert.assertEquals(acc.getUser().getFirstName(), daoAcc.getUser().getFirstName());
        }
    }
}
