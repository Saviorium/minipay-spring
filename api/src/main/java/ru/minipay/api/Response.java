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
public class Response {
    private final boolean success;
    private final String message;

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    Response() {
        this.success = false;
        this.message = null;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
