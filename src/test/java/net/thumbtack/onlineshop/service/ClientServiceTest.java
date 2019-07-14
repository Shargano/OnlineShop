package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.request.DepositRequest;
import net.thumbtack.onlineshop.dto.request.UpdateClientRequest;
import net.thumbtack.onlineshop.dto.response.LoginResponse;
import net.thumbtack.onlineshop.dto.response.account.ClientAccountInfoResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Client;
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

public class ClientServiceTest extends BaseServiceTest {
    private Client client = new Client("Sasha", "Michael", "mail", "address", "88005553535", "SayHello", "12321qq");
    private String sessionId = UUID.randomUUID().toString();

    @InjectMocks
    private ClientService clientService;

    @Before
    public void setUp() throws OnlineShopException {
        MockitoAnnotations.initMocks(this);

        when(clientDao.insert(any(Client.class))).thenReturn(client);
        when(clientDao.getById(any(Integer.class))).thenReturn(client);

        when(sessionDao.insert(any(Session.class))).thenReturn(new Session(sessionId, client));
        when(sessionDao.getById(any(String.class))).thenReturn(new Session(sessionId, client));
    }

    @Test
    public void testCreateClient() throws OnlineShopException {
        CreateClientRequest request = new CreateClientRequest("Sasha", "Michael", null, "mail", "address", "88005553535", "SayHello", "12321qq");
        LoginResponse loginResponse = clientService.create(request);
        ClientAccountInfoResponse response = (ClientAccountInfoResponse) loginResponse.getResponse();

        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getAddress(), response.getAddress());
        assertEquals(request.getPhone(), response.getPhone());
    }

    @Test(expected = OnlineShopException.class)
    public void testCreateClientDuplicateLogin() throws OnlineShopException {
        CreateClientRequest request = new CreateClientRequest("Sasha", "Michael", null, "mail", "address", "88005553535", "SayHello", "12321qq");
        clientService.create(request);

        doThrow(new OnlineShopException(OnlineShopErrorCode.USER_ALREADY_EXISTS)).when(clientDao).insert(client);
        clientService.create(request);
    }

    @Test
    public void testUpdateClient() throws OnlineShopException {
        UpdateClientRequest request = new UpdateClientRequest("Миша", "Dedun", "Sasha", "newMail", "newAddress", "89085551234", "12321qq", "12345678");

        ClientAccountInfoResponse response = clientService.update(UUID.randomUUID().toString(), request);
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getAddress(), response.getAddress());
        assertEquals(request.getPhone(), response.getPhone());
    }

    @Test(expected = OnlineShopException.class)
    public void testUpdateClientIncorrectPassword() throws OnlineShopException {
        UpdateClientRequest request = new UpdateClientRequest("Миша", "Dedun", "Sasha", "newMail", "newAddress", "89085551234", "INCORRECT", "12345678");
        clientService.update(UUID.randomUUID().toString(), request);
    }

    @Test
    public void testAddMoney() throws OnlineShopException {
        DepositRequest request = new DepositRequest(123);
        ClientAccountInfoResponse response = clientService.addMoney(sessionId, request);

        assertEquals((int) request.getDeposit(), response.getDeposit().getValue());
    }

    @Test
    public void testGetDeposit() throws OnlineShopException {
        ClientAccountInfoResponse response = clientService.getDeposit(sessionId);

        assertEquals(client.getFirstName(), response.getFirstName());
        assertEquals(client.getLastName(), response.getLastName());
        assertEquals(client.getPatronymic(), response.getPatronymic());
        assertEquals(client.getEmail(), response.getEmail());
        assertEquals(client.getAddress(), response.getAddress());
        assertEquals(client.getPhone(), response.getPhone());
        assertEquals(client.getDeposit(), response.getDeposit());
    }
}
