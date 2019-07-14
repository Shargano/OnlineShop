package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.request.DepositRequest;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GetDepositAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/deposits";

    @Test
    public void testGetDepositRegisterValue() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SayHello", "123321qq");
        String registerCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", registerCookie);

        ClientAccountInfoResponse response = template.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), ClientAccountInfoResponse.class).getBody();

        assertEquals(createClientRequest.getFirstName(), response.getFirstName());
        assertEquals(createClientRequest.getLastName(), response.getLastName());
        assertEquals(createClientRequest.getPatronymic(), response.getPatronymic());
        assertEquals(createClientRequest.getPhone(), response.getPhone());
        assertEquals(createClientRequest.getAddress(), response.getAddress());
        assertEquals(createClientRequest.getEmail(), response.getEmail());
        assertEquals(0, response.getDeposit().getValue());
    }

    @Test
    public void testGetDeposit() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SayHello", "123321qq");
        String registerCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", registerCookie);

        DepositRequest request = new DepositRequest(10000);
        template.exchange(url, HttpMethod.PUT, new HttpEntity<>(request, headers), ClientAccountInfoResponse.class);
        ClientAccountInfoResponse response = template.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), ClientAccountInfoResponse.class).getBody();

        assertEquals(createClientRequest.getFirstName(), response.getFirstName());
        assertEquals(createClientRequest.getLastName(), response.getLastName());
        assertEquals(createClientRequest.getPatronymic(), response.getPatronymic());
        assertEquals(createClientRequest.getPhone(), response.getPhone());
        assertEquals(createClientRequest.getAddress(), response.getAddress());
        assertEquals(createClientRequest.getEmail(), response.getEmail());
        assertEquals(10000, response.getDeposit().getValue());
    }

    @Test
    public void testGetDepositTwoClients() {
        CreateClientRequest createFirst = new CreateClientRequest("Первый", "Клиент", null, "mail@mail.com", "SomeAddress", "88005553535", "first", "123321qq");
        String registerCookie = getCookie(createClient(createFirst));
        HttpHeaders headers1 = new HttpHeaders();
        headers1.add("Cookie", registerCookie);

        CreateClientRequest createSecond = new CreateClientRequest("Второй", "Клиент", null, "mail@mail.com", "SomeAddress", "88005553535", "second", "123321qq");
        registerCookie = getCookie(createClient(createSecond));
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Cookie", registerCookie);

        DepositRequest request = new DepositRequest(10000);
        template.exchange(url, HttpMethod.PUT, new HttpEntity<>(request, headers1), ClientAccountInfoResponse.class);
        request = new DepositRequest(5000);
        template.exchange(url, HttpMethod.PUT, new HttpEntity<>(request, headers2), ClientAccountInfoResponse.class);

        ClientAccountInfoResponse responseFirst = template.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers1), ClientAccountInfoResponse.class).getBody();
        ClientAccountInfoResponse responseSecond = template.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers2), ClientAccountInfoResponse.class).getBody();

        assertNotEquals(responseFirst.getDeposit().getValue(), responseSecond.getDeposit().getValue());
        assertEquals(5000, responseSecond.getDeposit().getValue());
        assertEquals(10000, responseFirst.getDeposit().getValue());
    }

    @Test
    public void testGetDepositWithoutSession() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SayHello", "123321qq");
        String registerCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", registerCookie);


        DepositRequest request = new DepositRequest(10000);
        template.exchange(url, HttpMethod.PUT, new HttpEntity<>(request, headers), ClientAccountInfoResponse.class);
        logoutUser(registerCookie);
        try {
            template.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), ClientAccountInfoResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }
}