package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.request.DepositRequest;
import net.thumbtack.onlineshop.dto.response.ErrorItem;
import net.thumbtack.onlineshop.dto.response.account.ClientAccountInfoResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.ValidationErrorCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ClientAddMoneyAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/deposits";

    private void testInvalidRequest(DepositRequest request, String cookie, ValidationErrorCode code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, cookie);
        try {
            template.exchange(url, HttpMethod.PUT, new HttpEntity<>(request, headers), ClientAccountInfoResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            String responseCookie = getCookie(exc);
            ErrorItem response = getErrorResponse(exc);

            assertNull(responseCookie);
            assertEquals(400, exc.getStatusCode().value());
            checkErrorResponse(response, code);
        }
    }

    @Test
    public void testAddMoneyToClientDeposit() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SayHello", "123321qq");
        String registerCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", registerCookie);

        DepositRequest request = new DepositRequest(10000);
        ClientAccountInfoResponse updateResponse = template.exchange(url, HttpMethod.PUT, new HttpEntity<>(request, headers), ClientAccountInfoResponse.class).getBody();

        assertEquals(createClientRequest.getFirstName(), updateResponse.getFirstName());
        assertEquals(createClientRequest.getLastName(), updateResponse.getLastName());
        assertEquals(createClientRequest.getPatronymic(), updateResponse.getPatronymic());
        assertEquals(createClientRequest.getPhone(), updateResponse.getPhone());
        assertEquals(createClientRequest.getAddress(), updateResponse.getAddress());
        assertEquals(createClientRequest.getEmail(), updateResponse.getEmail());
        assertNotEquals(0, updateResponse.getDeposit().getValue());
        assertEquals(10000, updateResponse.getDeposit().getValue());
    }

    @Test
    public void testAddMoneyWithoutSession() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SayHello", "123321qq");
        String registerCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", registerCookie);

        DepositRequest request = new DepositRequest(10000);
        logoutUser(registerCookie);
        try {
            template.exchange(url, HttpMethod.PUT, new HttpEntity<>(request, headers), ClientAccountInfoResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }

    @Test()
    public void testAddMoneyToClientDepositInvalidValue() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "8-800-555-35-35", "SayHello", "123321qq");
        String registerCookie = getCookie(createClient(createClientRequest));
        DepositRequest request = new DepositRequest(0);
        testInvalidRequest(request, registerCookie, ValidationErrorCode.DEPOSIT);
        request.setDeposit(-10);
        testInvalidRequest(request, registerCookie, ValidationErrorCode.DEPOSIT);
    }

}