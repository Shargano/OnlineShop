package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.BasketItemRequest;
import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.request.ProductRequest;
import net.thumbtack.onlineshop.dto.response.BasketItemResponse;
import net.thumbtack.onlineshop.dto.response.ProductResponse;
import net.thumbtack.onlineshop.dto.response.PurchaseResponse;
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
public class ChangeProductBasketCountAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/baskets";

    private void testInvalidRequest(BasketItemRequest request, String cookie, ValidationErrorCode code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, cookie);
        try {
            template.exchange(url, HttpMethod.PUT, new HttpEntity<>(request, headers), PurchaseResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, code);
        }
    }

    @Test
    public void testChangeProductCount() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "AdminBest", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));

        ProductRequest createRequest = new ProductRequest("Name", 123, 100, null);
        ProductResponse productResponse = createProduct(createRequest, adminCookie).getBody();

        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SomeClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientCookie);

        BasketItemRequest createItemRequest = new BasketItemRequest(productResponse.getId(), productResponse.getName(), productResponse.getPrice(), null);
        List<BasketItemResponse> addResponses = template.exchange(url, HttpMethod.POST, new HttpEntity<>(createItemRequest, headers), new ParameterizedTypeReference<List<BasketItemResponse>>() {
        }).getBody();
        assertEquals(1, addResponses.size());
        assertEquals(1, (int) addResponses.get(0).getCount());

        BasketItemRequest updateRequest = new BasketItemRequest(productResponse.getId(), productResponse.getName(), productResponse.getPrice(), 66);
        List<BasketItemResponse> changeResponses = template.exchange(url, HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), new ParameterizedTypeReference<List<BasketItemResponse>>() {
        }).getBody();

        assertEquals(1, changeResponses.size());
        assertEquals(66, (int) changeResponses.get(0).getCount());
    }
}