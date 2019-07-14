package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.response.AllClientsInfoResponse;
import net.thumbtack.onlineshop.dto.response.EmptyResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GetAllClientsAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/clients";

    @Test
    public void testGetClientsInfo() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Клиент", "Клиентов", null, "mail@mail.com", "SomeAddress", "8-800-555-35-35", "SomeClient", "123321qq");
        createClient(createClientRequest);
        CreateAdminRequest request = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String cookie = getCookie(createAdmin(request));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);

        ResponseEntity<List<AllClientsInfoResponse>> responseEntity = template.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<List<AllClientsInfoResponse>>() {
        });
        List<AllClientsInfoResponse> responses = responseEntity.getBody();
        AllClientsInfoResponse clientResponse = responses.get(0);

        assertEquals(1, responses.size());
        assertEquals(clientResponse.getFirstName(), createClientRequest.getFirstName());
        assertEquals(clientResponse.getLastName(), createClientRequest.getLastName());
        assertEquals(clientResponse.getPatronymic(), createClientRequest.getPatronymic());
        assertEquals(clientResponse.getAddress(), createClientRequest.getAddress());
        assertEquals(clientResponse.getEmail(), createClientRequest.getEmail());
        assertEquals(clientResponse.getPhone(), createClientRequest.getPhone().replaceAll("-", ""));
    }

    @Test
    public void testNotAdminGetClientsInfo() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Клиент", "Клиентов", null, "mail@mail.com", "SomeAddress", "8-800-555-35-35", "SomeClient", "123321qq");
        createClient(createClientRequest);
        CreateClientRequest request = new CreateClientRequest("Ненастоящий", "Администратор", null, "mail@mail.com", "SomeAddress", "8-800-555-35-35", "NotRealAdmin", "123321qq");
        String cookie = getCookie(createClient(request));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        try {
            template.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<List<AllClientsInfoResponse>>() {
            });
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.USER_NOT_ADMIN);
        }
    }

    @Test
    public void testNotLoggedAdminGetClientsInfo() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Клиент", "Клиентов", null, "mail@mail.com", "SomeAddress", "8-800-555-35-35", "SomeClient", "123321qq");
        createClient(createClientRequest);
        CreateAdminRequest request = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String cookie = getCookie(createAdmin(request));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);

        template.exchange("http://localhost:" + port + "/api/sessions", HttpMethod.DELETE, new HttpEntity(null, headers), EmptyResponse.class);
        try {
            template.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<List<AllClientsInfoResponse>>() {
            });
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }
}