package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.BasketItemRequest;
import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.request.ProductRequest;
import net.thumbtack.onlineshop.dto.response.BasketItemResponse;
import net.thumbtack.onlineshop.dto.response.ProductResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GetBasketAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/baskets";

    @Test
    public void testGetBasket() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "AdminBest", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));

        ProductRequest createRequest = new ProductRequest("Name", 123, 100, null);
        ProductResponse productResponse = createProduct(createRequest, adminCookie).getBody();

        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientCookie);

        BasketItemRequest request = new BasketItemRequest(productResponse.getId(), productResponse.getName(), productResponse.getPrice(), null);
        template.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), new ParameterizedTypeReference<List<BasketItemResponse>>() {
        }).getBody();
        template.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), new ParameterizedTypeReference<List<BasketItemResponse>>() {
        }).getBody();
        List<BasketItemResponse> responses = template.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<List<BasketItemResponse>>() {
        }).getBody();

        assertEquals(2, responses.size());
        assertEquals(responses.get(0), responses.get(1));
    }

    @Test
    public void testEmptyGetBasket() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientCookie);

        List<BasketItemResponse> responses = template.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<List<BasketItemResponse>>() {
        }).getBody();

        assertEquals(0, responses.size());
    }

    @Test
    public void testGetBasketWithoutSession() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientCookie);

        logoutUser(clientCookie);
        try {
            template.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<List<BasketItemResponse>>() {
            });
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }
}