import ru.minipay.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AccountComparatorTest {

    private static Map<Currency, BigDecimal> exchangeRate;
    private Account a, b, c;

    {
        exchangeRate = new HashMap<>();
        exchangeRate.put(Currency.RUB, BigDecimal.ONE);
        exchangeRate.put(Currency.USD, new BigDecimal("65.6"));
        exchangeRate.put(Currency.EUR, new BigDecimal("74.8"));

        a = new Account(new User("Тест", "Тестовый", Gender.MALE, LocalDate.of(1995,1,1)), Currency.RUB);
        b = new Account(new User("Тест", "Тестовый", Gender.MALE, LocalDate.of(1999, 1, 1)), Currency.EUR);
        c = new Account(new User("Тест", "Тестовый", Gender.MALE, LocalDate.of(1999, 1, 1)), Currency.USD);
    }

    @Test
    public void testAccountsEquals() {
        Account d = a;
        Account[] accounts = {a, b, c, d};

        a.setBalance(1000L);
        b.setBalance(10L);
        c.setBalance(10L);

        Assert.assertNotEquals(a, b);
        Assert.assertEquals(a, d);

        Comparator<Account> comparator = new AccountComparator(exchangeRate);

        String before = b.getBalance().toString();

        Arrays.sort(accounts, comparator);
        Assert.assertTrue("Sorted not properly", accounts[0] == c);

        String after = b.getBalance().toString();
        Assert.assertTrue("Balance immutable at compare", (before.equals(after)));

        System.out.println(b);
    }

    @Test
    public void testExchangeRate() {
        a.setBalance(exchangeRate.get(b.getCurrency()).multiply(new BigDecimal(100L)));
        b.setBalance(100L);

        Comparator<Account> comparator = new AccountComparator(exchangeRate);

        Assert.assertTrue(comparator.compare(a, b) == 0);

        b.setBalance(exchangeRate.get(c.getCurrency()).multiply(new BigDecimal(999999999)));
        c.setBalance(exchangeRate.get(b.getCurrency()).multiply(new BigDecimal(999999999)));

        Assert.assertTrue(comparator.compare(b, c) == 0);
    }
}
