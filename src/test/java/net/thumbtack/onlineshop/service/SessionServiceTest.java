package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dao.UserDao;
import net.thumbtack.onlineshop.dto.request.LoginRequest;
import net.thumbtack.onlineshop.dto.response.LoginResponse;
import net.thumbtack.onlineshop.dto.response.account.AdminAccountInfoResponse;
import net.thumbtack.onlineshop.dto.response.account.ClientAccountInfoResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Administrator;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SessionServiceTest extends BaseServiceTest {
    private Administrator admin = new Administrator("Sasha", "Michael", "DevOps", "SayHello", "12321qq");
    private Client client = new Client("Sasha", "Michael", "mail", "address", "88005553535", "SayHello", "12321qq");

    @Mock
    private UserDao userDao;

    @InjectMocks
    private SessionService sessionService;

    @Before
    public void setUp() throws OnlineShopException {
        MockitoAnnotations.initMocks(this);
        String sessionId = UUID.randomUUID().toString();

        when(userDao.getByLogin(any(String.class))).thenReturn(admin);
        when(adminDao.getById(any(Integer.class))).thenReturn(admin);
        when(clientDao.getById(any(Integer.class))).thenReturn(client);

        when(sessionDao.insert(any(Session.class))).thenReturn(new Session(sessionId, admin));
        when(sessionDao.getById(any(String.class))).thenReturn(new Session(sessionId, admin));
    }

    @Test
    public void testCreateSession() throws OnlineShopException {
        LoginRequest request = new LoginRequest("SayHello", "12321qq");
        LoginResponse loginResponse = sessionService.create(request);
        AdminAccountInfoResponse response = (AdminAccountInfoResponse) loginResponse.getResponse();

        assertEquals(admin.getFirstName(), response.getFirstName());
        assertEquals(admin.getLastName(), response.getLastName());
        assertEquals(admin.getPatronymic(), response.getPatronymic());
        assertEquals(admin.getPosition(), response.getPosition());
    }

    @Test
    public void testCreateSessionWrongPassword() {
        LoginRequest request = new LoginRequest("SayHello", "21qq");
        try {
            sessionService.create(request);
        } catch (OnlineShopException e) {
            assertEquals(OnlineShopErrorCode.PASSWORD_FAILED, e.getErrorCode());
        }
    }

    @Test
    public void testGetAdminAccountInfo() throws OnlineShopException {
        LoginRequest request = new LoginRequest("SayHello", "12321qq");
        LoginResponse loginResponse = sessionService.create(request);
        String sessionId = loginResponse.getSessionId();
        AdminAccountInfoResponse response = (AdminAccountInfoResponse) sessionService.getAccountInfo(sessionId);

        assertEquals(admin.getFirstName(), response.getFirstName());
        assertEquals(admin.getLastName(), response.getLastName());
        assertEquals(admin.getPatronymic(), response.getPatronymic());
        assertEquals(admin.getPosition(), response.getPosition());
    }

    @Test
    public void testGetClientAccountInfo() throws OnlineShopException {
        when(sessionDao.getById(any(String.class))).thenReturn(new Session(UUID.randomUUID().toString(), client));
        ClientAccountInfoResponse response = (ClientAccountInfoResponse) sessionService.getAccountInfo(UUID.randomUUID().toString());

        assertEquals(client.getFirstName(), response.getFirstName());
        assertEquals(client.getLastName(), response.getLastName());
        assertEquals(client.getPatronymic(), response.getPatronymic());
        assertEquals(client.getAddress(), response.getAddress());
        assertEquals(client.getEmail(), response.getEmail());
        assertEquals(client.getPhone(), response.getPhone());
    }
}