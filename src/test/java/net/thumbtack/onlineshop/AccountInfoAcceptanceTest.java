package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.response.EmptyResponse;
import net.thumbtack.onlineshop.dto.response.account.AccountInfoResponse;
import net.thumbtack.onlineshop.dto.response.account.AdminAccountInfoResponse;
import net.thumbtack.onlineshop.dto.response.account.ClientAccountInfoResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AccountInfoAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private String url = "http://localhost:" + port + "/api/accounts";

    @Test
    public void testGetAdminInfo() {
        CreateAdminRequest request = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String cookie = getCookie(createAdmin(request));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);

        AdminAccountInfoResponse response = template.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), AdminAccountInfoResponse.class).getBody();

        assertNotNull(response.getId());
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getPosition(), response.getPosition());
    }

    @Test
    public void testGetClientInfo() {
        CreateClientRequest request = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SayHello", "123321qq");
        String cookie = getCookie(createClient(request));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);

        ClientAccountInfoResponse response = template.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), ClientAccountInfoResponse.class).getBody();
        assertNotNull(response.getId());
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getAddress(), response.getAddress());
        assertEquals(request.getPhone(), response.getPhone());
        assertEquals(0, response.getDeposit().getValue());
    }

    @Test
    public void testGetUnknownUserInfo() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "JAVASESSIONID=" + UUID.randomUUID().toString());
        try {
            template.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), AccountInfoResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            assertNull(getCookie(exc));
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }

    @Test
    public void testGetNotLoggedUserInfo() {
        CreateAdminRequest request = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", getCookie(createAdmin(request)));

        template.exchange("http://localhost:" + port + "/api/sessions", HttpMethod.DELETE, new HttpEntity<>(null, headers), EmptyResponse.class);
        try {
            template.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), AccountInfoResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            assertNull(getCookie(exc));
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }

}