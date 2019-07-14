package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.*;
import net.thumbtack.onlineshop.dto.response.BasketItemResponse;
import net.thumbtack.onlineshop.dto.response.BuyBasketResponse;
import net.thumbtack.onlineshop.dto.response.ProductResponse;
import net.thumbtack.onlineshop.dto.response.account.ClientAccountInfoResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BuyBasketAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/purchases/baskets";
    private final String basketUrl = "http://localhost:" + port + "/api/baskets";
    private final String depositUrl = "http://localhost:" + port + "/api/deposits";

    @Test
    public void testBuyBasket() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "AdminBest", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));

        ProductRequest createFirst = new ProductRequest("First", 1, 100, null);
        ProductResponse firstResponse = createProduct(createFirst, adminCookie).getBody();
        ProductRequest createSecond = new ProductRequest("Second", 1, 100, null);
        ProductResponse secondResponse = createProduct(createSecond, adminCookie).getBody();

        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientCookie);

        BasketItemRequest firstRequest = new BasketItemRequest(firstResponse.getId(), firstResponse.getName(), firstResponse.getPrice(), 100);
        BasketItemRequest secondRequest = new BasketItemRequest(secondResponse.getId(), secondResponse.getName(), secondResponse.getPrice(), 100);
        template.exchange(basketUrl, HttpMethod.POST, new HttpEntity<>(firstRequest, headers), new ParameterizedTypeReference<List<BasketItemResponse>>() {
        }).getBody();
        List<BasketItemResponse> basket = template.exchange(basketUrl, HttpMethod.POST, new HttpEntity<>(secondRequest, headers), new ParameterizedTypeReference<List<BasketItemResponse>>() {
        }).getBody();

        DepositRequest depositRequest = new DepositRequest(10000);
        template.exchange(depositUrl, HttpMethod.PUT, new HttpEntity<>(depositRequest, headers), ClientAccountInfoResponse.class);

        List<PurchaseProductRequest> requests = new ArrayList<>();
        requests.add(new PurchaseProductRequest(basket.get(0).getId(), basket.get(0).getName(), basket.get(0).getPrice(), null));
        requests.add(new PurchaseProductRequest(basket.get(1).getId(), basket.get(1).getName(), basket.get(1).getPrice(), null));
        BuyBasketResponse response = template.exchange(url, HttpMethod.POST, new HttpEntity<>(requests, headers), BuyBasketResponse.class).getBody();

        assertEquals(2, response.getBought().size());
        assertEquals(100, (int) response.getBought().get(0).getCount());
        assertEquals(100, (int) response.getBought().get(1).getCount());
        assertEquals(0, response.getRemaining().size());
    }

    @Test
    public void testBuyBasketNotEnoughMoney() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "AdminBest", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));

        ProductRequest createRequest = new ProductRequest("Name", 123, 100, null);
        ProductResponse productResponse = createProduct(createRequest, adminCookie).getBody();

        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientCookie);

        BasketItemRequest request = new BasketItemRequest(productResponse.getId(), productResponse.getName(), productResponse.getPrice(), null);
        List<BasketItemResponse> basket = template.exchange(basketUrl, HttpMethod.POST, new HttpEntity<>(request, headers), new ParameterizedTypeReference<List<BasketItemResponse>>() {
        }).getBody();

        List<PurchaseProductRequest> requests = new ArrayList<>();
        requests.add(new PurchaseProductRequest(basket.get(0).getId(), basket.get(0).getName(), basket.get(0).getPrice(), basket.get(0).getCount()));
        BuyBasketResponse response = template.exchange(url, HttpMethod.POST, new HttpEntity<>(requests, headers), BuyBasketResponse.class).getBody();

        assertEquals(0, response.getBought().size());
        assertEquals(1, response.getRemaining().size());
    }

    @Test
    public void testBuyBasketNotEnoughProductCount() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "AdminBest", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));

        ProductRequest createRequest = new ProductRequest("Name", 123, 5, null);
        ProductResponse productResponse = createProduct(createRequest, adminCookie).getBody();

        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientCookie);

        BasketItemRequest request = new BasketItemRequest(productResponse.getId(), productResponse.getName(), productResponse.getPrice(), 9);
        template.exchange(basketUrl, HttpMethod.POST, new HttpEntity<>(request, headers), new ParameterizedTypeReference<List<BasketItemResponse>>() {
        });
        List<BasketItemResponse> basket = template.exchange(basketUrl, HttpMethod.POST, new HttpEntity<>(request, headers), new ParameterizedTypeReference<List<BasketItemResponse>>() {
        }).getBody();

        DepositRequest depositRequest = new DepositRequest(10000);
        template.exchange(depositUrl, HttpMethod.PUT, new HttpEntity<>(depositRequest, headers), ClientAccountInfoResponse.class);

        List<PurchaseProductRequest> requests = new ArrayList<>();
        requests.add(new PurchaseProductRequest(basket.get(0).getId(), basket.get(0).getName(), basket.get(0).getPrice(), basket.get(0).getCount()));
        requests.add(new PurchaseProductRequest(basket.get(1).getId(), basket.get(1).getName(), basket.get(1).getPrice(), basket.get(1).getCount()));
        BuyBasketResponse response = template.exchange(url, HttpMethod.POST, new HttpEntity<>(requests, headers), BuyBasketResponse.class).getBody();

        assertEquals(0, response.getBought().size());
        assertEquals(2, response.getRemaining().size());
    }

    @Test
    public void testBuyNotWholeBasket() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "AdminBest", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));

        ProductRequest createFirst = new ProductRequest("First", 123, 100, null);
        ProductResponse firstResponse = createProduct(createFirst, adminCookie).getBody();
        ProductRequest createSecond = new ProductRequest("Second", 123, 100, null);
        ProductResponse secondResponse = createProduct(createSecond, adminCookie).getBody();

        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientCookie);

        BasketItemRequest firstRequest = new BasketItemRequest(firstResponse.getId(), firstResponse.getName(), firstResponse.getPrice(), 10);
        BasketItemRequest secondRequest = new BasketItemRequest(secondResponse.getId(), secondResponse.getName(), secondResponse.getPrice(), 45);
        template.exchange(basketUrl, HttpMethod.POST, new HttpEntity<>(firstRequest, headers), new ParameterizedTypeReference<List<BasketItemResponse>>() {
        }).getBody();
        List<BasketItemResponse> basket = template.exchange(basketUrl, HttpMethod.POST, new HttpEntity<>(secondRequest, headers), new ParameterizedTypeReference<List<BasketItemResponse>>() {
        }).getBody();

        DepositRequest depositRequest = new DepositRequest(10000);
        template.exchange(depositUrl, HttpMethod.PUT, new HttpEntity<>(depositRequest, headers), ClientAccountInfoResponse.class);

        List<PurchaseProductRequest> requests = new ArrayList<>();
        requests.add(new PurchaseProductRequest(basket.get(0).getId(), basket.get(0).getName(), basket.get(0).getPrice(), basket.get(0).getCount()));
        requests.add(new PurchaseProductRequest(basket.get(1).getId(), basket.get(1).getName(), basket.get(1).getPrice(), 40));
        BuyBasketResponse response = template.exchange(url, HttpMethod.POST, new HttpEntity<>(requests, headers), BuyBasketResponse.class).getBody();

        assertEquals(2, response.getBought().size());
        assertEquals(1, response.getRemaining().size());
    }

    @Test
    public void testBuyBasketWithWrongFields() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "AdminBest", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));

        ProductRequest createFirst = new ProductRequest("First", 1, 100, null);
        ProductResponse firstResponse = createProduct(createFirst, adminCookie).getBody();
        ProductRequest createSecond = new ProductRequest("Second", 1, 100, null);
        ProductResponse secondResponse = createProduct(createSecond, adminCookie).getBody();

        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientCookie);

        BasketItemRequest firstRequest = new BasketItemRequest(firstResponse.getId(), firstResponse.getName(), firstResponse.getPrice(), 100);
        BasketItemRequest secondRequest = new BasketItemRequest(secondResponse.getId(), secondResponse.getName(), secondResponse.getPrice(), 100);
        template.exchange(basketUrl, HttpMethod.POST, new HttpEntity<>(firstRequest, headers), new ParameterizedTypeReference<List<BasketItemResponse>>() {
        }).getBody();
        List<BasketItemResponse> basket = template.exchange(basketUrl, HttpMethod.POST, new HttpEntity<>(secondRequest, headers), new ParameterizedTypeReference<List<BasketItemResponse>>() {
        }).getBody();

        DepositRequest depositRequest = new DepositRequest(10000);
        template.exchange(depositUrl, HttpMethod.PUT, new HttpEntity<>(depositRequest, headers), ClientAccountInfoResponse.class);

        List<PurchaseProductRequest> requests = new ArrayList<>();
        requests.add(new PurchaseProductRequest(basket.get(0).getId(), "IncorrectName", basket.get(0).getPrice(), null));
        requests.add(new PurchaseProductRequest(basket.get(1).getId(), basket.get(1).getName(), 99990, null));
        BuyBasketResponse response = template.exchange(url, HttpMethod.POST, new HttpEntity<>(requests, headers), BuyBasketResponse.class).getBody();

        assertEquals(2, response.getRemaining().size());
    }
}