import ru.minipay.model.*;
import org.junit.Assert;
import org.junit.Test;
import ru.minipay.model.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class AccountComparatorTest {
    private Random rnd;
    private static Map<Currency, BigDecimal> exchangeRate;

    {
        exchangeRate = new HashMap<>();
        exchangeRate.put(Currency.RUB, BigDecimal.ONE);
        exchangeRate.put(Currency.USD, new BigDecimal("65.6"));
        exchangeRate.put(Currency.EUR, new BigDecimal("74.8"));

        rnd = new Random();
    }

    @Test
    public void checkRelations() {
        Account a = getTestAccount(Currency.RUB);
        Account b = getTestAccount(Currency.RUB);
        Account c = getTestAccount(Currency.RUB);
        a.setBalance(BigDecimal.valueOf(12345, 2));
        b.setBalance(BigDecimal.valueOf(12345, 2));
        Comparator<Account> comparator = new AccountComparator(exchangeRate);

        Assert.assertEquals("Check reflexivity", 0, comparator.compare(a, a));

        Assert.assertEquals("Check symmetry", 0, comparator.compare(a, b));
        Assert.assertEquals("Check symmetry", 0, comparator.compare(b, a));

        a.setBalance(BigDecimal.valueOf(10001, 2));
        b.setBalance(BigDecimal.valueOf(20001, 2));
        a.setBalance(BigDecimal.valueOf(30001, 2));
        Assert.assertEquals(1, comparator.compare(a, b) );
        Assert.assertEquals(-1, comparator.compare(c, b));
        Assert.assertEquals(1, comparator.compare(a, c) );

        a.setBalance(100L);
        b.setBalance(a.getBalance().negate());
        Assert.assertEquals("Check negative value", 1, comparator.compare(a, b));
        b.setBalance(b.getBalance().negate());
        Assert.assertEquals("Check sign negate", 0, comparator.compare(a, b));
    }

    @Test
    public void randomTests() {
        int N = 10000;
        Account a = getTestAccount(Currency.EUR);
        Account b = getTestAccount(Currency.USD);
        Comparator<Account> comparator = new AccountComparator(exchangeRate);
        BigDecimal rndA, rndB;

        for(int i = 0; i < N; i++) {
            rndA = BigDecimal.valueOf(rnd.nextLong(),2);
            rndB = BigDecimal.valueOf(rnd.nextLong(),2);
            a.setBalance(rndA.multiply(exchangeRate.get(b.getCurrency())));
            b.setBalance(rndB.multiply(exchangeRate.get(a.getCurrency())));
            Assert.assertEquals(i + ": Compare " + rndA + " " + rndB + " as "+ a.getBalance() + "EUR " + b.getBalance() + "USD",
                    comparator.compare(a, b),
                    rndA.compareTo(rndB));
        }
    }

    @Test
    public void sortRandomAccounts() {
        Account[] testArray = new Account[100];
        for(int i = 0; i < testArray.length; i++) {
            testArray[i] = getTestAccount(Currency.RUB);
            testArray[i].setBalance(Math.abs(rnd.nextLong()));
        }
        Comparator<Account> comparator = new AccountComparator(exchangeRate);
        Account min = testArray[0];
        for (Account acc : testArray) {
            if(acc.getBalance().compareTo(min.getBalance()) < 0) {
                min = acc;
            }
        }
        Arrays.sort(testArray, comparator);
        Assert.assertEquals("Check manual sort and using a comparator",  min.getBalance(), testArray[0].getBalance());
    }

    @Test
    public void exchangeRateEquals() {
        Account accRUB = getTestAccount(Currency.RUB), accUSD = getTestAccount(Currency.USD), accEUR = getTestAccount(Currency.EUR);

        BigDecimal balance100RUBinUSD = exchangeRate.get(accUSD.getCurrency()).multiply(new BigDecimal(100L));
        accRUB.setBalance(balance100RUBinUSD);
        accUSD.setBalance(100L);

        Comparator<Account> comparator = new AccountComparator(exchangeRate);
        Assert.assertEquals("Test Exchange rate: USD to RUB", 0, comparator.compare(accRUB, accUSD));

        //multiply same number to exchange rate - should get same number but in different currencies
        accUSD.setBalance(exchangeRate.get(accEUR.getCurrency()).multiply(new BigDecimal(999999999999L)));
        accEUR.setBalance(exchangeRate.get(accUSD.getCurrency()).multiply(new BigDecimal(999999999999L)));

        Assert.assertEquals("Test same big value in USD and EUR balances",0, comparator.compare(accUSD, accEUR));

        accEUR.setBalance(100L);
        accUSD.setBalance(100L);
        Assert.assertNotEquals("Same balance in different currencies",0, comparator.compare(accUSD, accEUR));
    }

    @Test
    public void balanceImmutableAfterCompare() {
        Account accUSD = getTestAccount(Currency.USD), accEUR = getTestAccount(Currency.EUR);
        String originalValue = "123.45";
        BigDecimal newValue = new BigDecimal(originalValue);
        accUSD.setBalance(newValue);
        accEUR.setBalance(newValue);

        Comparator<Account> comparator = new AccountComparator(exchangeRate);

        comparator.compare(accEUR, accUSD);
        newValue = accEUR.getBalance();
        Assert.assertEquals(new BigDecimal(originalValue), newValue);

        comparator.compare(accUSD, accEUR);
        newValue = accUSD.getBalance();
        Assert.assertEquals(new BigDecimal(originalValue), newValue);
    }

    private Account getTestAccount(Currency currency) {
        LocalDate randomDate = LocalDate.of(
                1900 + rnd.nextInt(100),
                1 + rnd.nextInt(12),
                1 + rnd.nextInt(28));
        return new Account(
                new User("Клиент", "Тестовый", rnd.nextBoolean()?Gender.MALE:Gender.FEMALE, randomDate),
                currency);
    }

    private BigDecimal getInCurrency(BigDecimal value, Account account) {
        return exchangeRate.get(account.getCurrency()).multiply(value);
    }
}
