package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.*;
import net.thumbtack.onlineshop.dto.response.CategoryResponse;
import net.thumbtack.onlineshop.dto.response.ProductResponse;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UpdateProductAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/products/";

    private void testInvalidRequest(UpdateProductRequest request, Integer id, String cookie, ValidationErrorCode code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        try {
            template.exchange(url + id, HttpMethod.PUT, new HttpEntity<>(request, headers), ProductResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, code);
        }
    }

    @Test
    public void testUpdateProductName() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", registerCookie);

        ProductRequest createRequest = new ProductRequest("Name", 123, 0, null);
        ProductResponse createResponse = createProduct(createRequest, registerCookie).getBody();

        UpdateProductRequest updateRequest = new UpdateProductRequest("NewUnusualName", null, null, null);
        ProductResponse updateResponse = template.exchange(url + createResponse.getId(), HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), ProductResponse.class).getBody();

        assertNotNull(updateResponse.getId());
        assertEquals(updateRequest.getName(), updateResponse.getName());
        assertEquals(createResponse.getPrice(), updateResponse.getPrice());
        assertEquals(createResponse.getCount(), updateResponse.getCount());
        assertEquals(createResponse.getCategories(), updateResponse.getCategories());
    }

    @Test
    public void testUpdateProduct() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", registerCookie);

        CategoryRequest categoryRequest = new CategoryRequest("category", null);
        CategoryResponse categoryResponse = createCategory(categoryRequest, registerCookie).getBody();
        List<Integer> categoriesId = new ArrayList<>();
        categoriesId.add(categoryResponse.getId());

        ProductRequest createRequest = new ProductRequest("Name", 123, 0, null);
        ProductResponse createResponse = createProduct(createRequest, registerCookie).getBody();

        UpdateProductRequest updateRequest = new UpdateProductRequest(null, 10, 99, categoriesId);
        ProductResponse updateResponse = template.exchange(url + createResponse.getId(), HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), ProductResponse.class).getBody();

        assertEquals(createResponse.getId(), updateResponse.getId());
        assertEquals(createRequest.getName(), updateResponse.getName());
        assertEquals((int) updateRequest.getPrice(), updateResponse.getPrice());
        assertEquals((int) updateRequest.getCount(), updateResponse.getCount());
        assertEquals(updateRequest.getCategories(), updateResponse.getCategories());
    }

    @Test
    public void testUpdateProductSetEmptyCategories() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", registerCookie);

        CategoryRequest categoryRequest = new CategoryRequest("category", null);
        CategoryResponse categoryResponse = createCategory(categoryRequest, registerCookie).getBody();
        List<Integer> categoriesId = new ArrayList<>();
        categoriesId.add(categoryResponse.getId());
        ProductRequest createRequest = new ProductRequest("Name", 123, 0, categoriesId);
        ProductResponse createResponse = createProduct(createRequest, registerCookie).getBody();
        assertEquals(categoryResponse.getId(), (int) createResponse.getCategories().get(0));

        UpdateProductRequest updateRequest = new UpdateProductRequest(null, 10, 99, new ArrayList<>());
        ProductResponse updateResponse = template.exchange(url + createResponse.getId(), HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), ProductResponse.class).getBody();
        ProductResponse getResponse = template.exchange(url + createResponse.getId(), HttpMethod.GET, new HttpEntity<>(null, headers), ProductResponse.class).getBody();

        assertEquals(createResponse.getName(), getResponse.getName());
        assertEquals((int) updateRequest.getPrice(), getResponse.getPrice());
        assertEquals((int) updateRequest.getCount(), getResponse.getCount());
        assertEquals(0, getResponse.getCategories().size());
    }

    @Test
    public void testUpdateProductSetNotExistingCategory() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", registerCookie);

        List<Integer> categoriesId = new ArrayList<>();
        categoriesId.add(1231);

        ProductRequest createRequest = new ProductRequest("Name", 123, 0, null);
        ProductResponse createResponse = createProduct(createRequest, registerCookie).getBody();

        UpdateProductRequest updateRequest = new UpdateProductRequest("NewUnusualName", 10, 99, categoriesId);
        try {
            template.exchange(url + createResponse.getId(), HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), ProductResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.CATEGORY_NOT_EXISTS);
        }
    }

    @Test
    public void testUpdateProductNotAdmin() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));
        ProductRequest createRequest = new ProductRequest("Name", 123, 0, null);
        ProductResponse createResponse = createProduct(createRequest, adminCookie).getBody();
        logoutUser(adminCookie);

        UpdateProductRequest updateRequest = new UpdateProductRequest("NewUnusualName", 10, 99, null);
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "NotAdmin", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", clientCookie);
        try {
            template.exchange(url + createResponse.getId(), HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), ProductResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.USER_NOT_ADMIN);
        }
    }

    @Test
    public void testUpdateProductWithoutSession() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", registerCookie);
        ProductRequest createRequest = new ProductRequest("Name", 123, 0, null);
        ProductResponse createResponse = createProduct(createRequest, registerCookie).getBody();

        UpdateProductRequest updateRequest = new UpdateProductRequest("NewUnusualName", 10, 99, null);
        logoutUser(registerCookie);
        try {
            template.exchange(url + createResponse.getId(), HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), ProductResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }

    @Test
    public void testUpdateProductInvalidName() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        ProductRequest createRequest = new ProductRequest("Name", 123, 0, null);
        ProductResponse createResponse = createProduct(createRequest, registerCookie).getBody();
        UpdateProductRequest request = new UpdateProductRequest("", null, null, null);
        testInvalidRequest(request, createResponse.getId(), registerCookie, ValidationErrorCode.NAME);
        request.setName("    ");
        testInvalidRequest(request, createResponse.getId(), registerCookie, ValidationErrorCode.NAME);
        request.setName("It is Very very long string, don't now, what can I write here, just some letters");
    }

    @Test
    public void testUpdateProductInvalidPrice() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        ProductRequest createRequest = new ProductRequest("Name", 123, 0, null);
        ProductResponse createResponse = createProduct(createRequest, registerCookie).getBody();
        UpdateProductRequest request = new UpdateProductRequest(null, 0, null, null);
        testInvalidRequest(request, createResponse.getId(), registerCookie, ValidationErrorCode.PRICE);
        request.setPrice(-100);
        testInvalidRequest(request, createResponse.getId(), registerCookie, ValidationErrorCode.PRICE);
    }

    @Test
    public void testUpdateProductInvalidNameCount() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        ProductRequest createRequest = new ProductRequest("Name", 123, 0, null);
        ProductResponse createResponse = createProduct(createRequest, registerCookie).getBody();
        UpdateProductRequest request = new UpdateProductRequest(null, null, -44, null);
        testInvalidRequest(request, createResponse.getId(), registerCookie, ValidationErrorCode.COUNT);
    }
}