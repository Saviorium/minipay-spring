package ru.minipay.service;

import ru.minipay.dao.AccountDao;
import ru.minipay.model.Account;
import ru.minipay.api.Currency;
import ru.minipay.api.FundTransferResponse;

import java.math.BigDecimal;
import java.util.ConcurrentModificationException;
import java.util.UUID;

public class FundTransferServiceImpl implements FundTransferService{
    private final AccountDao dao;
    private final FundExchangeService exchangeService;

    public FundTransferServiceImpl(AccountDao dao, FundExchangeService exchangeService) {
        this.dao = dao;
        this.exchangeService = exchangeService;
    }

    @Override
    public FundTransferResponse makeTransfer(UUID fromId, UUID toId, Currency currency, BigDecimal amount) {
        if(fromId.equals(toId)) {
            return new FundTransferResponse(false, "From and To accounts are the same");
        }
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
        try {
            dao.insert(from);
        } catch (ConcurrentModificationException e) {
            return new FundTransferResponse(false, "Collision while trying to modify from balance");
        }

        BigDecimal amountToInCurrency = exchangeService.exchange(amount, currency, to.getCurrency());
        to.setBalance(to.getBalance().add(amountToInCurrency));
        try {
            dao.insert(to);
        } catch (Exception e) { //TODO: handle DB exceptions properly
            //try to return funds
            dao.getById(fromId);
            from.setBalance(from.getBalance().add(amountFromInCurrency));
            try {
                dao.insert(from); //FIXME: can fail too if DB is dead
                return new FundTransferResponse(false, "Collision while trying to modify from balance. Funds returned to from balance");
            } catch (ConcurrentModificationException ex) {
                return new FundTransferResponse(false, "Funds are subtracted from " + fromId + " but didn't returned!");
                //throw new IllegalStateException("Funds are subtracted from " + fromId + " but didn't returned!");
            }
        }
        return new FundTransferResponse(true,
                "Sent " + amount + " from " + fromId + "(" + from.getBalance() + ")"
                        + " to " + toId + "(" + to.getBalance());
    }
}
