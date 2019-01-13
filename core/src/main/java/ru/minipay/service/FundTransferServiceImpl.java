package ru.minipay.service;

import ru.minipay.dao.AccountDao;
import ru.minipay.model.Account;
import ru.minipay.api.Currency;
import ru.minipay.api.FundTransferResponse;

import java.math.BigDecimal;
import java.util.UUID;

public class FundTransferServiceImpl implements FundTransferService{
    private final AccountDao dao;
    private final FundExchangeService exchangeService;

    public FundTransferServiceImpl(AccountDao dao, FundExchangeService exchangeService) {
        this.dao = dao;
        this.exchangeService = exchangeService;
    }

    @Override
    public synchronized FundTransferResponse makeTransfer(UUID fromId, UUID toId, Currency currency, BigDecimal amount) { //TODO: better concurrency
        Account from = dao.getById(fromId);
        Account to = dao.getById(toId);
        if(from == null || to == null) {
            return new FundTransferResponse(false, "User not found");
        }
        BigDecimal amountFromInCurrency = exchangeService.exchange(amount, currency, from.getCurrency());
        if(from.getBalance().subtract(amountFromInCurrency).signum() < 1) {
            return new FundTransferResponse(false, "From balance if negative");
        }
        from.setBalance(from.getBalance().subtract(amountFromInCurrency));
        dao.insert(from);

        BigDecimal amountToInCurrency = exchangeService.exchange(amount, currency, to.getCurrency());
        to.setBalance(to.getBalance().add(amountToInCurrency));
        try {
            dao.insert(to);
        } catch (Exception e) { //TODO: handle DB exceptions properly
            from.setBalance(from.getBalance().add(amountFromInCurrency));
            dao.insert(from); //FIXME: can fail too if DB is dead
            return new FundTransferResponse(false, e.getMessage());
        }
        return new FundTransferResponse(true);
    }
}
