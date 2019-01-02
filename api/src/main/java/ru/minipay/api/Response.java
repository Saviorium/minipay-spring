package ru.minipay.api;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FundTransferResponse.class, name = "FundTransfer"),
        @JsonSubTypes.Type(value = CreateAccountRequest.class, name = "CreateAccount")
})
public interface Response {}
