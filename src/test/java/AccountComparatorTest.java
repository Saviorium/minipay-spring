import ru.minipay.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;

public class AccountComparatorTest {
    @Test
    public void testAccountsEquals() {
        Account a = new Account(new User("Тест", "Тестовый", Gender.MALE, LocalDate.of(1995,1,1)), Currency.RUB);
        Account b = new Account(new User("Тест", "Тестовый", Gender.MALE, LocalDate.of(1999, 1, 1)), Currency.RUB);
        a.setBalance(1000L);
        b.setBalance(100L);
        Account c = a;
        Account[] accounts = {a, b, c};

        Assert.assertNotEquals(a, b);
        Assert.assertEquals(a, c);
        Arrays.sort(accounts);
        Assert.assertTrue(accounts[0] == b);
    }
}
