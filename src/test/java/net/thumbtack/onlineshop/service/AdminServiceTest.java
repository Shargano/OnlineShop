package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.request.UpdateAdminRequest;
import net.thumbtack.onlineshop.dto.response.LoginResponse;
import net.thumbtack.onlineshop.dto.response.account.AdminAccountInfoResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Administrator;
import net.thumbtack.onlineshop.model.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class AdminServiceTest extends BaseServiceTest {
    private Administrator admin = new Administrator("Sasha", "Mic", "Leader", "SayHello", "12321qq");

    @InjectMocks
    private AdminService adminService;

    @Before
    public void setUp() throws OnlineShopException {
        MockitoAnnotations.initMocks(this);
        String sessionId = UUID.randomUUID().toString();

        when(adminDao.insert(any(Administrator.class))).thenReturn(admin);
        when(adminDao.getById(any(Integer.class))).thenReturn(admin);

        when(sessionDao.insert(any(Session.class))).thenReturn(new Session(sessionId, admin));
        when(sessionDao.getById(any(String.class))).thenReturn(new Session(sessionId, admin));
    }

    @Test
    public void testCreateAdmin() throws OnlineShopException {
        CreateAdminRequest request = new CreateAdminRequest("Sasha", "Mic", null, "Leader", "SayHello", "12321qq");
        LoginResponse loginResponse = adminService.create(request);
        AdminAccountInfoResponse response = (AdminAccountInfoResponse) loginResponse.getResponse();

        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getPosition(), response.getPosition());
    }

    @Test(expected = OnlineShopException.class)
    public void testCreateAdminDuplicateLogin() throws OnlineShopException {
        CreateAdminRequest request = new CreateAdminRequest("Sasha", "Mic", null, "Leader", "SayHello", "12321qq");
        adminService.create(request);

        doThrow(new OnlineShopException(OnlineShopErrorCode.USER_ALREADY_EXISTS)).when(adminDao).insert(admin);
        adminService.create(request);
    }

    @Test
    public void testUpdateAdmin() throws OnlineShopException {
        UpdateAdminRequest request = new UpdateAdminRequest("Миша", "Dedun", "Sasha", "DevOps", "12321qq", "12345678");

        AdminAccountInfoResponse response = adminService.update(UUID.randomUUID().toString(), request);
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getPosition(), response.getPosition());
    }

    @Test(expected = OnlineShopException.class)
    public void testUpdateAdminIncorrectPassword() throws OnlineShopException {
        UpdateAdminRequest request = new UpdateAdminRequest("Миша", "Dedun", "Sasha", "DevOps", "121qq", "12345678");
        adminService.update(UUID.randomUUID().toString(), request);
    }

}