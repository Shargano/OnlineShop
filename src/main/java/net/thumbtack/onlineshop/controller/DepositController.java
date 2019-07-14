package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.request.DepositRequest;
import net.thumbtack.onlineshop.dto.response.account.ClientAccountInfoResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/deposits")
public class DepositController {
    private final ClientService clientService;

    @Autowired
    public DepositController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ClientAccountInfoResponse addMoney(@CookieValue("JAVASESSIONID") String sessionId,
                                              @Valid @RequestBody DepositRequest request) throws OnlineShopException {
        return clientService.addMoney(sessionId, request);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ClientAccountInfoResponse getDeposit(@CookieValue("JAVASESSIONID") String sessionId) throws OnlineShopException {
        return clientService.getDeposit(sessionId);
    }
}