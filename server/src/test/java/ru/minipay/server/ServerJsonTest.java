package ru.minipay.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Assert;
import org.junit.Test;
import ru.minipay.model.Account;
import ru.minipay.model.SampleAccountGenerator;
import ru.minipay.api.FundTransferResponse;

public class ServerJsonTest {
    private final static SampleAccountGenerator accGen = SampleAccountGenerator.getInstance();

    @Test
    public void testAccountToJson() throws Exception {
        Account acc1 = accGen.getTestAccount();
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        String accJson = objectMapper.writeValueAsString(acc1);
        Account acc2 = objectMapper.readValue(accJson, Account.class);
        Assert.assertEquals(acc1, acc2);
    }

    @Test
    public void testFundTransferResultJson() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        FundTransferResponse result = new FundTransferResponse(true, "Message");
        String json = objectMapper.writeValueAsString(result);
        FundTransferResponse parsedResult = objectMapper.readValue(json, FundTransferResponse.class);
        Assert.assertEquals(result.isSuccess(), parsedResult.isSuccess());
        Assert.assertEquals(result.getMessage(), parsedResult.getMessage());
    }
}
