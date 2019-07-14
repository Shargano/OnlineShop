package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.request.UpdateClientRequest;
import net.thumbtack.onlineshop.dto.response.AllClientsInfoResponse;
import net.thumbtack.onlineshop.dto.response.LoginResponse;
import net.thumbtack.onlineshop.dto.response.account.ClientAccountInfoResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ClientAccountInfoResponse registerClient(@Valid @RequestBody CreateClientRequest request, HttpServletResponse servletResponse) throws OnlineShopException {
        LoginResponse loginResponse = clientService.create(request);
        servletResponse.addCookie(new Cookie("JAVASESSIONID", loginResponse.getSessionId()));
        return (ClientAccountInfoResponse) loginResponse.getResponse();
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ClientAccountInfoResponse updateClient(@CookieValue("JAVASESSIONID") String sessionId, @Valid @RequestBody UpdateClientRequest request) throws OnlineShopException {
        return clientService.update(sessionId, request);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AllClientsInfoResponse> getAllClients(@CookieValue("JAVASESSIONID") String sessionId) throws OnlineShopException {
        return clientService.getAll(sessionId);
    }
}