package ru.minipay.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import ru.minipay.model.Currency;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

public class FundTransferRequestTest {
    @Test
    public void TestFundTransferRequestJsonConversion() throws IOException {
        FundTransferRequest request = new FundTransferRequest(UUID.randomUUID(), UUID.randomUUID(), Currency.RUB, BigDecimal.valueOf(123456L));
        ObjectMapper jsonParser = new ObjectMapper();
        String json = jsonParser.writeValueAsString(request);
        Assert.assertFalse(json.isEmpty());
        Assert.assertTrue(json.contains("123456"));
        Assert.assertTrue(json.contains("RUB"));
        request = jsonParser.readValue(json, FundTransferRequest.class);
    }
}
