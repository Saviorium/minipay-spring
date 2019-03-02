package ru.minipay.service;

import org.apache.logging.log4j.LogManager;
import ru.minipay.dao.AccountDao;
import ru.minipay.dao.TransactionDao;
import ru.minipay.exceptions.DataAccessException;
import ru.minipay.model.Account;
import ru.minipay.api.Currency;
import ru.minipay.api.FundTransferResponse;
import ru.minipay.model.Transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.UUID;

public class FundTransferServiceImpl implements FundTransferService{
    private final AccountDao accountDao;
    private final TransactionDao transactionDao;
    private final FundExchangeService exchangeService;

    public FundTransferServiceImpl(AccountDao accountDao, TransactionDao transactionDao, FundExchangeService exchangeService) {
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
        this.exchangeService = exchangeService;
    }

    @Override
    public FundTransferResponse makeTransfer(UUID fromId, UUID toId, Currency currency, BigDecimal amount) {
        if(fromId.equals(toId)) {
            return new FundTransferResponse(false, "From and To accounts are the same");
        }
        //Get accounts
        Account from, to;
        try {
            from = accountDao.getById(fromId);
            to = accountDao.getById(toId);
        } catch (DataAccessException e) {
            return new FundTransferResponse(false, e.getMessage());
        }
        if(from == null || to == null) {
            return new FundTransferResponse(false, "User not found");
        }
        //Balance handling
        BigDecimal amountFromInCurrency = exchangeService.exchange(amount, currency, from.getCurrency());
        if(from.getBalance().subtract(amountFromInCurrency).signum() < 1) {
            return new FundTransferResponse(false, "From balance if negative");
        }
        from.setBalance(from.getBalance().subtract(amountFromInCurrency));

        BigDecimal amountToInCurrency = exchangeService.exchange(amount, currency, to.getCurrency());
        to.setBalance(to.getBalance().add(amountToInCurrency));
        //Save changes
        try {
            List<Account> accounts = new ArrayList<>();
            accounts.add(from);
            accounts.add(to);
            accountDao.insert(accounts);
        } catch (DataAccessException | ConcurrentModificationException e) {
            LogManager.getLogger().error("Fund transfer failed", e);
            return new FundTransferResponse(false, e.getMessage());
        }

        transactionDao.insert(new Transaction(fromId, toId, currency, amount, Instant.now()));
        return new FundTransferResponse(true,
                "Sent " + amount + " from " + fromId + "(" + from.getBalance() + ")"
                        + " to " + toId + "(" + to.getBalance());
    }
}
