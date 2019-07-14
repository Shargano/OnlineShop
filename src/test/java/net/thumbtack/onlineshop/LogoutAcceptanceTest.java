package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.response.EmptyResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LogoutAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/sessions";

    @Test
    public void testLogoutUser() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", registerCookie);
        ResponseEntity<EmptyResponse> logoutResponseEntity = template.exchange(url, HttpMethod.DELETE, new HttpEntity<>(null, headers), EmptyResponse.class);

        String cookie = getCookie(logoutResponseEntity);
        assertNull(cookie);
        assertEquals(200, logoutResponseEntity.getStatusCode().value());
    }

    @Test
    public void testLogoutNotLoggedUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "JAVASESSIONID=" + UUID.randomUUID().toString());
        try {
            template.exchange(url, HttpMethod.DELETE, new HttpEntity<>(null, headers), EmptyResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            assertNull(getCookie(exc));
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }

    @Test
    public void testLogoutUserTwice() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", registerCookie);
        template.exchange(url, HttpMethod.DELETE, new HttpEntity<>(null, headers), EmptyResponse.class);
        try {
            template.exchange(url, HttpMethod.DELETE, new HttpEntity<>(null, headers), EmptyResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            assertNull(getCookie(exc));
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }
}