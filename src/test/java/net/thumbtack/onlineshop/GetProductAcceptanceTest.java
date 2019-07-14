package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.CategoryRequest;
import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.request.ProductRequest;
import net.thumbtack.onlineshop.dto.response.CategoryResponse;
import net.thumbtack.onlineshop.dto.response.ProductResponse;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GetProductAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/products/";

    @Test
    public void testAdminGetProduct() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);

        ProductRequest createRequest = new ProductRequest("name", 11, 11, null);
        ProductResponse createResponse = createProduct(createRequest, registerCookie).getBody();
        ProductResponse getResponse = template.exchange(url + createResponse.getId(), HttpMethod.GET, new HttpEntity<>(null, headers), ProductResponse.class).getBody();

        assertEquals(createRequest.getName(), getResponse.getName());
        assertEquals(createRequest.getPrice(), getResponse.getPrice());
        assertEquals(createResponse.getCount(), getResponse.getCount());
        assertEquals(createResponse.getCategories(), getResponse.getCategories());
    }

    @Test
    public void testClientGetProduct() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));

        ProductRequest createRequest = new ProductRequest("name", 11, 11, null);
        ProductResponse createResponse = createProduct(createRequest, adminCookie).getBody();

        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "CallMyName", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, clientCookie);
        ProductResponse getResponse = template.exchange(url + createResponse.getId(), HttpMethod.GET, new HttpEntity<>(null, headers), ProductResponse.class).getBody();

        assertEquals(createRequest.getName(), getResponse.getName());
        assertEquals(createRequest.getPrice(), getResponse.getPrice());
        assertEquals(createResponse.getCount(), getResponse.getCount());
        assertEquals(createResponse.getCategories(), getResponse.getCategories());
    }

    @Test
    public void testGetProductWithCategory() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);

        CategoryRequest categoryRequest = new CategoryRequest("category", null);
        CategoryResponse categoryResponse = createCategory(categoryRequest, registerCookie).getBody();
        List<Integer> categories = new ArrayList<>();
        categories.add(categoryResponse.getId());

        ProductRequest createRequest = new ProductRequest("name", 11, 11, categories);
        ProductResponse createResponse = createProduct(createRequest, registerCookie).getBody();
        ProductResponse getResponse = template.exchange(url + createResponse.getId(), HttpMethod.GET, new HttpEntity<>(null, headers), ProductResponse.class).getBody();

        assertEquals(createRequest.getName(), getResponse.getName());
        assertEquals(createRequest.getPrice(), getResponse.getPrice());
        assertEquals(createResponse.getCount(), getResponse.getCount());
        assertEquals(createResponse.getCategories(), getResponse.getCategories());
    }

    @Test
    public void testGetNonExistingProduct() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);
        try {
            template.exchange(url + 123, HttpMethod.GET, new HttpEntity<>(null, headers), ProductResponse.class);
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.PRODUCT_NOT_EXISTS);
        }
    }

    @Test
    public void testGetProductWithoutSession() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);

        ProductRequest createRequest = new ProductRequest("name", 11, 11, null);
        ProductResponse createResponse = createProduct(createRequest, registerCookie).getBody();
        logoutUser(registerCookie);
        try {
            template.exchange(url + createResponse.getId(), HttpMethod.GET, new HttpEntity<>(null, headers), ProductResponse.class);
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }
}