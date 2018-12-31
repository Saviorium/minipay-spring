package ru.minipay.model;

import ru.minipay.api.Currency;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class SampleAccountGenerator {
    private static final SampleAccountGenerator INSTANCE = new SampleAccountGenerator();
    private static final ThreadLocalRandom rnd = ThreadLocalRandom.current();

    private SampleAccountGenerator() {}

    public static SampleAccountGenerator getInstance() {
        return INSTANCE;
    }

    public Account getTestAccount(Currency currency) {
        return new Account(getTestUser(), currency);
    }

    public Account getTestAccount() {
        return getTestAccount(Currency.RUB);
    }

    public User getTestUser() {
        LocalDate randomDate = LocalDate.of(
                1900 + rnd.nextInt(100),
                1 + rnd.nextInt(12),
                1 + rnd.nextInt(28));
        return new User("Клиент", "Тестовый", rnd.nextBoolean()?Gender.MALE:Gender.FEMALE, randomDate);
    }
}
