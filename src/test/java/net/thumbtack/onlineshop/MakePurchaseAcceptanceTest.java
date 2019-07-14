package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.*;
import net.thumbtack.onlineshop.dto.response.ProductResponse;
import net.thumbtack.onlineshop.dto.response.PurchaseResponse;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MakePurchaseAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/purchases";

    private void testInvalidRequest(PurchaseProductRequest request, String cookie, ValidationErrorCode code) {
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
    public void testMakePurchase() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "AdminBest", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));

        ProductRequest createRequest = new ProductRequest("Name", 123, 100, null);
        ProductResponse productResponse = createProduct(createRequest, adminCookie).getBody();

        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientCookie);

        DepositRequest depositRequest = new DepositRequest(10000);
        template.exchange("http://localhost:" + port + "/api/deposits", HttpMethod.PUT, new HttpEntity<>(depositRequest, headers), ClientAccountInfoResponse.class).getBody();

        PurchaseProductRequest purchaseRequest = new PurchaseProductRequest(productResponse.getId(), productResponse.getName(), productResponse.getPrice(), 10);
        PurchaseResponse purchaseResponse = template.exchange(url, HttpMethod.POST, new HttpEntity<>(purchaseRequest, headers), PurchaseResponse.class).getBody();

        assertEquals(purchaseRequest.getId(), purchaseResponse.getId());
        assertEquals(purchaseRequest.getName(), purchaseResponse.getName());
        assertEquals(purchaseRequest.getPrice(), purchaseResponse.getPrice());
        assertEquals(purchaseRequest.getCount(), purchaseResponse.getCount());
    }

    @Test
    public void testMakePurchaseProductNotExists() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientCookie);

        DepositRequest depositRequest = new DepositRequest(10000);
        template.exchange("http://localhost:" + port + "/api/deposits", HttpMethod.PUT, new HttpEntity<>(depositRequest, headers), ClientAccountInfoResponse.class).getBody();

        PurchaseProductRequest purchaseRequest = new PurchaseProductRequest(0, "some", 123435664, 10);
        try {
            template.exchange(url, HttpMethod.POST, new HttpEntity<>(purchaseRequest, headers), PurchaseResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.PRODUCT_NOT_EXISTS);
        }
    }

    @Test
    public void testMakePurchaseProductIncorrectName() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "AdminBest", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));

        ProductRequest createRequest = new ProductRequest("Name", 123, 100, null);
        ProductResponse productResponse = createProduct(createRequest, adminCookie).getBody();

        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientCookie);

        DepositRequest depositRequest = new DepositRequest(10000);
        template.exchange("http://localhost:" + port + "/api/deposits", HttpMethod.PUT, new HttpEntity<>(depositRequest, headers), ClientAccountInfoResponse.class).getBody();

        PurchaseProductRequest purchaseRequest = new PurchaseProductRequest(productResponse.getId(), "noname", productResponse.getPrice(), 10);
        try {
            template.exchange(url, HttpMethod.POST, new HttpEntity<>(purchaseRequest, headers), PurchaseResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.PRODUCT_NAME_INCORRECT);
        }
    }

    @Test
    public void testMakePurchaseProductIncorrectPrice() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "AdminBest", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));

        ProductRequest createRequest = new ProductRequest("Name", 123, 100, null);
        ProductResponse productResponse = createProduct(createRequest, adminCookie).getBody();

        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientCookie);

        DepositRequest depositRequest = new DepositRequest(10000);
        template.exchange("http://localhost:" + port + "/api/deposits", HttpMethod.PUT, new HttpEntity<>(depositRequest, headers), ClientAccountInfoResponse.class).getBody();

        PurchaseProductRequest purchaseRequest = new PurchaseProductRequest(productResponse.getId(), productResponse.getName(), 10, 10);
        try {
            template.exchange(url, HttpMethod.POST, new HttpEntity<>(purchaseRequest, headers), PurchaseResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.PRODUCT_PRICE_INCORRECT);
        }
    }

    @Test
    public void testMakePurchaseProductNotEnoughCount() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "AdminBest", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));

        ProductRequest createRequest = new ProductRequest("Name", 123, 10, null);
        ProductResponse productResponse = createProduct(createRequest, adminCookie).getBody();

        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientCookie);

        DepositRequest depositRequest = new DepositRequest(10000);
        template.exchange("http://localhost:" + port + "/api/deposits", HttpMethod.PUT, new HttpEntity<>(depositRequest, headers), ClientAccountInfoResponse.class).getBody();

        PurchaseProductRequest purchaseRequest = new PurchaseProductRequest(productResponse.getId(), productResponse.getName(), productResponse.getPrice(), 12);
        try {
            template.exchange(url, HttpMethod.POST, new HttpEntity<>(purchaseRequest, headers), PurchaseResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.NOT_ENOUGH_PRODUCT_COUNT);
        }
    }

    @Test
    public void testMakePurchaseProductNotEnoughMoney() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "AdminBest", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));

        ProductRequest createRequest = new ProductRequest("Name", 1230, 1000, null);
        ProductResponse productResponse = createProduct(createRequest, adminCookie).getBody();

        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientCookie);

        DepositRequest depositRequest = new DepositRequest(10000);
        template.exchange("http://localhost:" + port + "/api/deposits", HttpMethod.PUT, new HttpEntity<>(depositRequest, headers), ClientAccountInfoResponse.class).getBody();

        PurchaseProductRequest purchaseRequest = new PurchaseProductRequest(productResponse.getId(), productResponse.getName(), productResponse.getPrice(), 12);
        try {
            template.exchange(url, HttpMethod.POST, new HttpEntity<>(purchaseRequest, headers), PurchaseResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.NOT_ENOUGH_MONEY);
        }
    }

    @Test
    public void testMakePurchaseWithoutSession() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientCookie);

        PurchaseProductRequest purchaseRequest = new PurchaseProductRequest(0, "some", 12, null);
        logoutUser(clientCookie);
        try {
            template.exchange(url, HttpMethod.POST, new HttpEntity<>(purchaseRequest, headers), PurchaseResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }

    @Test
    public void testMakePurchaseInvalidName() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        PurchaseProductRequest request = new PurchaseProductRequest(1, null, 100, 10);
        testInvalidRequest(request, clientCookie, ValidationErrorCode.NAME);
        request.setName("");
        testInvalidRequest(request, clientCookie, ValidationErrorCode.NAME);
        request.setName("           ");
        testInvalidRequest(request, clientCookie, ValidationErrorCode.NAME);
    }

    @Test
    public void testMakePurchaseInvalidPrice() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        PurchaseProductRequest request = new PurchaseProductRequest(1, "some", 0, 10);
        testInvalidRequest(request, clientCookie, ValidationErrorCode.PRICE);
        request.setPrice(-10);
        testInvalidRequest(request, clientCookie, ValidationErrorCode.PRICE);
    }

    @Test
    public void testMakePurchaseInvalidCount() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        PurchaseProductRequest request = new PurchaseProductRequest(1, "some", 10, 0);
        testInvalidRequest(request, clientCookie, ValidationErrorCode.COUNT);
        request.setCount(-10);
        testInvalidRequest(request, clientCookie, ValidationErrorCode.COUNT);
    }
}