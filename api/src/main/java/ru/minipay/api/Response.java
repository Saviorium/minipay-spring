package ru.minipay.api;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FundTransferResponse.class, name = "FundTransfer"),
        @JsonSubTypes.Type(value = CreateAccountResponse.class, name = "CreateAccount"),
        @JsonSubTypes.Type(value = GetBalanceResponse.class, name = "GetBalance"),
        @JsonSubTypes.Type(value = ErrorResponse.class, name = "ErrorResponse")
})
public interface Response {}
