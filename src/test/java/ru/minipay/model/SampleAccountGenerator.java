package ru.minipay.model;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class SampleAccountGenerator {
    private ThreadLocalRandom rnd = ThreadLocalRandom.current();

    public Account getTestAccount(Currency currency) {
        return new Account(getTestUser(), currency);
    }

    public User getTestUser() {
        LocalDate randomDate = LocalDate.of(
                1900 + rnd.nextInt(100),
                1 + rnd.nextInt(12),
                1 + rnd.nextInt(28));
        return new User("Клиент", "Тестовый", rnd.nextBoolean()?Gender.MALE:Gender.FEMALE, randomDate);
    }
}
