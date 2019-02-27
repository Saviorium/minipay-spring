package ru.minipay.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.minipay.api.Currency;
import ru.minipay.model.Account;
import ru.minipay.model.Gender;
import ru.minipay.model.SampleAccountGenerator;
import ru.minipay.model.User;

import java.time.LocalDate;
import java.util.LinkedList;

public class AccountDaoDbImplTest {
    private static AccountDao accountDao;

    private static final SampleAccountGenerator accGen = SampleAccountGenerator.getInstance();

    @BeforeClass
    public static void setUp() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost/minipay");
        ds.setUsername("minipay");
        ds.setPassword("sVWneZ4eA");
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);

        accountDao = new AccountDaoDbImpl(ds);
    }

    @Test
    public void testAccountInsert() {
        Account acc = new Account(new User("Имя", "Фамилия", Gender.FEMALE, LocalDate.now()), Currency.EUR);
        accountDao.insert(acc);
        Account daoAcc = accountDao.getById(acc.getId());
        Assert.assertEquals("Имя", daoAcc.getUser().getFirstName());
        Assert.assertEquals("Фамилия", daoAcc.getUser().getLastName());
        Assert.assertEquals(Gender.FEMALE, daoAcc.getUser().getGender());
        Assert.assertEquals(Currency.EUR, daoAcc.getCurrency());
    }

    @Test
    public void testInMemMultipleInsert() {
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
