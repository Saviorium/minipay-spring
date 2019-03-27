package ru.minipay.minipay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import ru.minipay.minipay.api.Currency;
import ru.minipay.minipay.api.FundTransferResponse;
import ru.minipay.minipay.exceptions.DataAccessException;
import ru.minipay.minipay.model.Account;
import ru.minipay.minipay.model.Gender;
import ru.minipay.minipay.model.User;
import ru.minipay.minipay.service.FundTransferService;
import ru.minipay.minipay.service.UserAccountsServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
public class MinipayApplication {
    private final FundTransferService fundTransferService;
    private final UserAccountsServiceImpl userAccountsService;

    @Autowired
    public MinipayApplication(UserAccountsServiceImpl userAccountsService, FundTransferService fundTransferService) {
        this.fundTransferService = fundTransferService;
        this.userAccountsService = userAccountsService;
    }

    public Account createAccount(User user, Currency currency, BigDecimal initBalance) throws DataAccessException {
        return this.userAccountsService.createAccount(user, currency, initBalance);
    }

    public Account createTestAccount() throws DataAccessException { //TODO: delete me
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

    public Account getAccount(UUID accId) throws DataAccessException {
        return userAccountsService.getAccount(accId);
    }

}
