package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.CategoryRequest;
import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.request.ProductRequest;
import net.thumbtack.onlineshop.dto.response.CategoryResponse;
import net.thumbtack.onlineshop.dto.response.EmptyResponse;
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

import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DeleteProductAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/products/";

    @Test
    public void testDeleteProduct() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);

        ProductRequest createRequest = new ProductRequest("name", 100, 10, null);
        ProductResponse response = createProduct(createRequest, registerCookie).getBody();

        template.exchange(url + response.getId(), HttpMethod.DELETE, new HttpEntity<>(null, headers), EmptyResponse.class);
        try {
            template.exchange(url + response.getId(), HttpMethod.GET, new HttpEntity<>(null, headers), ProductResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.PRODUCT_NOT_EXISTS);
        }
    }

    @Test
    public void testDeleteProductWithCategory() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);

        CategoryRequest createCategory = new CategoryRequest("parent", null);
        CategoryResponse createCategoryResponse = createCategory(createCategory, registerCookie).getBody();
        List<Integer> categories = new ArrayList<>();
        categories.add(createCategoryResponse.getId());

        ProductRequest createRequest = new ProductRequest("name", 100, 10, categories);
        ProductResponse response = createProduct(createRequest, registerCookie).getBody();

        template.exchange(url + response.getId(), HttpMethod.DELETE, new HttpEntity<>(null, headers), EmptyResponse.class);
        try {
            template.exchange(url + response.getId(), HttpMethod.GET, new HttpEntity<>(null, headers), ProductResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.PRODUCT_NOT_EXISTS);
        }
    }

    @Test
    public void testDeleteNotExistingProduct() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);
        try {
            template.exchange(url + 3333, HttpMethod.DELETE, new HttpEntity<>(null, headers), EmptyResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.PRODUCT_NOT_EXISTS);
        }
    }

    @Test
    public void testDeleteProductNotAdmin() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));
        ProductRequest createRequest = new ProductRequest("name", 100, 10, null);
        ProductResponse response = createProduct(createRequest, adminCookie).getBody();
        logoutUser(adminCookie);

        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "CallMyName", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, clientCookie);
        try {
            template.exchange(url + response.getId(), HttpMethod.DELETE, new HttpEntity<>(null, headers), EmptyResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.USER_NOT_ADMIN);
        }
    }

    @Test
    public void testDeleteProductWithoutSession() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);
        ProductRequest createRequest = new ProductRequest("name", 100, 10, null);
        ProductResponse response = createProduct(createRequest, registerCookie).getBody();
        logoutUser(registerCookie);
        try {
            template.exchange(url + response.getId(), HttpMethod.DELETE, new HttpEntity<>(null, headers), EmptyResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }
}