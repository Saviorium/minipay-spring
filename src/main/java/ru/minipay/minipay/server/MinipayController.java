package ru.minipay.minipay.server;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.minipay.minipay.MinipayApplication;
import ru.minipay.minipay.api.*;
import ru.minipay.minipay.exceptions.DataAccessException;
import ru.minipay.minipay.model.Account;

import java.util.UUID;

@RestController
public class MinipayController {
    @Autowired
    private MinipayApplication application;

    @GetMapping(value = "/CreateAccount")
    public Response CreateAccount() {
        Response response;
        Account acc = null; //TODO: create real accounts here
        try {
            acc = application.createTestAccount();
            if(acc == null) {
                response = new CreateAccountResponse(false, null, "Cannot create account. Got null account.");
            } else {
                response = new CreateAccountResponse(true, acc.getId(), "");
            }
        } catch (DataAccessException e) {
            LogManager.getLogger().error("Got DataAccessException", e);
            response = new ErrorResponse(false, "Error accessing the database");
        }
        return response;
    }

    @GetMapping(value = "/GetBalance/{id}")
    public Response GetBalance(@PathVariable UUID id) {
        Response response;
        try {
            Account acc = application.getAccount(id);
            response = new GetBalanceResponse(acc.getBalance(), acc.getCurrency());
        } catch (DataAccessException e) {
            LogManager.getLogger().error("Got DataAccessException", e);
            response = new ErrorResponse(false, "Error accessing the database");
        }
        return response;
    }

    @PostMapping(value = "/FundTransfer")
    public Response FundTransfer(@RequestBody FundTransferRequest request) {
        Response response;
        response = application.makeTransfer(
                request.getFromAccId(), request.getToAccId(),
                request.getCurrency(), request.getAmount());
        return response;
    }
}
