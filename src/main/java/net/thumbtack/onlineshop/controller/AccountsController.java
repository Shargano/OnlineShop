package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.response.account.AccountInfoResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountsController {
    private final SessionService sessionService;

    @Autowired
    public AccountsController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public AccountInfoResponse getInfo(@CookieValue("JAVASESSIONID") String sessionId) throws OnlineShopException {
        return sessionService.getAccountInfo(sessionId);
    }
}