package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.BasketItemRequest;
import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.request.ProductRequest;
import net.thumbtack.onlineshop.dto.response.BasketItemResponse;
import net.thumbtack.onlineshop.dto.response.ProductResponse;
import net.thumbtack.onlineshop.dto.response.PurchaseResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.ValidationErrorCode;
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
public class AddProductToBasketAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/baskets";

    private void testInvalidRequest(BasketItemRequest request, String cookie, ValidationErrorCode code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, cookie);
        try {
            template.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), PurchaseResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, code);
        }
    }

    @Test
    public void testAddProductToBasket() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "AdminBest", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));

        ProductRequest createRequest = new ProductRequest("Name", 123, 100, null);
        ProductResponse productResponse = createProduct(createRequest, adminCookie).getBody();

        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientCookie);

        BasketItemRequest request = new BasketItemRequest(productResponse.getId(), productResponse.getName(), productResponse.getPrice(), null);
        List<BasketItemResponse> responses = template.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), new ParameterizedTypeReference<List<BasketItemResponse>>() {
        }).getBody();

        assertEquals(1, responses.size());
        assertEquals(request.getId(), responses.get(0).getId());
        assertEquals(request.getName(), responses.get(0).getName());
        assertEquals(request.getPrice(), responses.get(0).getPrice());
        assertEquals(1, (int) responses.get(0).getCount());
    }

    @Test
    public void testAddProductToBasketWithoutSession() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "AdminBest", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));

        ProductRequest createRequest = new ProductRequest("Name", 123, 100, null);
        ProductResponse productResponse = createProduct(createRequest, adminCookie).getBody();

        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientCookie);

        logoutUser(clientCookie);
        try {
            BasketItemRequest request = new BasketItemRequest(productResponse.getId(), productResponse.getName(), productResponse.getPrice(), 12);
            template.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), new ParameterizedTypeReference<List<BasketItemResponse>>() {
            });
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }

    @Test
    public void testAddToBasketNotExistingProduct() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientCookie);

        BasketItemRequest request = new BasketItemRequest(0, "name", 1234, 12);
        try {
            template.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), new ParameterizedTypeReference<List<BasketItemResponse>>() {
            });
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.PRODUCT_NOT_EXISTS);
        }
    }

    @Test
    public void testAddProductToBasketInvalidName() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        BasketItemRequest request = new BasketItemRequest(10, null, 1234, 12);
        testInvalidRequest(request, clientCookie, ValidationErrorCode.NAME);
        request.setName("");
        testInvalidRequest(request, clientCookie, ValidationErrorCode.NAME);
        request.setName("           ");
        testInvalidRequest(request, clientCookie, ValidationErrorCode.NAME);
    }

    @Test
    public void testAddProductToBasketInvalidPrice() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        BasketItemRequest request = new BasketItemRequest(10, "some", 0, 12);
        testInvalidRequest(request, clientCookie, ValidationErrorCode.PRICE);
        request.setPrice(-10);
        testInvalidRequest(request, clientCookie, ValidationErrorCode.PRICE);
    }

    @Test
    public void testAddProductToBasketInvalidCount() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        BasketItemRequest request = new BasketItemRequest(10, "some", 10, 0);
        testInvalidRequest(request, clientCookie, ValidationErrorCode.COUNT);
        request.setCount(-10);
        testInvalidRequest(request, clientCookie, ValidationErrorCode.COUNT);
    }
}