package ru.minipay;

import ru.minipay.model.Account;
import ru.minipay.api.Currency;
import ru.minipay.model.User;
import ru.minipay.api.FundTransferResponse;
import ru.minipay.service.FundTransferService;
import ru.minipay.service.UserAccountsService;

import java.math.BigDecimal;
import java.util.UUID;

//test account generation
import java.util.concurrent.ThreadLocalRandom;
import java.time.LocalDate;
import ru.minipay.model.Gender;

public class MinipayApplication {
    private final FundTransferService fundTransferService;
    private final UserAccountsService userAccountsService;

    public MinipayApplication(UserAccountsService userAccountsService, FundTransferService fundTransferService) {
        this.fundTransferService = fundTransferService;
        this.userAccountsService = userAccountsService;
    }

    public Account createAccount(User user, Currency currency, BigDecimal initBalance) {
        return this.userAccountsService.createAccount(user, currency, initBalance);
    }

    public Account createTestAccount() { //TODO: delete me
        return createAccount(getTestUser(), Currency.RUB, BigDecimal.valueOf(100L));
    }

    private User getTestUser() {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        LocalDate randomDate = LocalDate.of(
                1900 + rnd.nextInt(100),
                1 + rnd.nextInt(12),
                1 + rnd.nextInt(28));
        return new User("Клиент", "Тестовый", rnd.nextBoolean()? Gender.MALE:Gender.FEMALE, randomDate);
    }

    public FundTransferResponse makeTransfer(UUID fromAccId, UUID toAccId, Currency currency, BigDecimal amount) {
        return fundTransferService.makeTransfer(fromAccId, toAccId, currency, amount);
    }
}
