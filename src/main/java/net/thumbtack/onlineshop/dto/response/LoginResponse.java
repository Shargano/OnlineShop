package net.thumbtack.onlineshop.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.thumbtack.onlineshop.dto.response.account.AccountInfoResponse;

public class LoginResponse {
    @JsonIgnore
    private String sessionId;
    private AccountInfoResponse response;

    public LoginResponse() {
    }

    public LoginResponse(String sessionId, AccountInfoResponse response) {
        this.sessionId = sessionId;
        this.response = response;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public AccountInfoResponse getResponse() {
        return response;
    }

    public void setResponse(AccountInfoResponse response) {
        this.response = response;
    }
}