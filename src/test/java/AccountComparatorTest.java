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
        a.setBalance(1000L);
        b.setBalance(10L);
        c.setBalance(10L);
    }

    @Test
    public void testAccountsEquals() {
        Account d = a;
        Account[] accounts = {a, b, c, d};

        Assert.assertNotEquals(a, b);
        Assert.assertEquals(a, d);

        Comparator<Account> comparator = new AccountComparator(exchangeRate);

        String before = new String(b.getBalance().toString());

        Arrays.sort(accounts, comparator);
        Assert.assertTrue("Sorted not properly", accounts[0] == c);

        String after = new String(b.getBalance().toString());
        Assert.assertTrue("Balance immutable at compare", (before.equals(after)));
    }
}
