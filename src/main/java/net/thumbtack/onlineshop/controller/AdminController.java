package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.request.UpdateAdminRequest;
import net.thumbtack.onlineshop.dto.response.LoginResponse;
import net.thumbtack.onlineshop.dto.response.account.AdminAccountInfoResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/admins")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AdminAccountInfoResponse registerAdmin(@Valid @RequestBody CreateAdminRequest request, HttpServletResponse servletResponse) throws OnlineShopException {
        LoginResponse loginResponse = adminService.create(request);
        servletResponse.addCookie(new Cookie("JAVASESSIONID", loginResponse.getSessionId()));
        return (AdminAccountInfoResponse) loginResponse.getResponse();
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AdminAccountInfoResponse updateAdmin(@CookieValue("JAVASESSIONID") String sessionId, @Valid @RequestBody UpdateAdminRequest request) throws OnlineShopException {
        return adminService.update(sessionId, request);
    }

}