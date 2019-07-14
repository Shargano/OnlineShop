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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DeleteCategoryAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/categories/";

    @Test
    public void testDeleteCategory() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);

        CategoryRequest createRequest = new CategoryRequest("name", null);
        CategoryResponse response = createCategory(createRequest, registerCookie).getBody();
        template.exchange(url + response.getId(), HttpMethod.DELETE, new HttpEntity<>(null, headers), EmptyResponse.class);
        try {
            template.exchange(url + response.getId(), HttpMethod.GET, new HttpEntity<>(null, headers), CategoryResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.CATEGORY_NOT_EXISTS);
        }
    }

    @Test
    public void testDeleteCategoryWithChild() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);
        CategoryRequest createParent = new CategoryRequest("parent", null);
        CategoryResponse parent = createCategory(createParent, registerCookie).getBody();
        CategoryRequest createChild = new CategoryRequest("child", parent.getId());
        CategoryResponse child = createCategory(createChild, registerCookie).getBody();
        template.exchange(url + parent.getId(), HttpMethod.DELETE, new HttpEntity<>(null, headers), EmptyResponse.class);
        try {
            template.exchange(url + child.getId(), HttpMethod.GET, new HttpEntity<>(null, headers), CategoryResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.CATEGORY_NOT_EXISTS);
        }
    }

    @Test
    public void testDeleteSubCategory() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);
        CategoryRequest createParent = new CategoryRequest("parent", null);
        CategoryResponse parent = createCategory(createParent, registerCookie).getBody();
        CategoryRequest createChild = new CategoryRequest("child", parent.getId());
        CategoryResponse child = createCategory(createChild, registerCookie).getBody();
        template.exchange(url + child.getId(), HttpMethod.DELETE, new HttpEntity<>(null, headers), EmptyResponse.class);
        try {
            template.exchange(url + child.getId(), HttpMethod.GET, new HttpEntity<>(null, headers), CategoryResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.CATEGORY_NOT_EXISTS);
        }
        CategoryResponse getResponse = template.exchange(url + parent.getId(), HttpMethod.GET, new HttpEntity<>(null, headers), CategoryResponse.class).getBody();
        assertEquals(parent.getId(), getResponse.getId());
        assertEquals(parent.getName(), getResponse.getName());
        assertEquals(parent.getParentName(), getResponse.getParentName());
        assertEquals(parent.getParentId(), getResponse.getParentId());
    }

    @Test
    public void testDeleteCategoryWithProduct() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);
        CategoryRequest createCategoryRequest = new CategoryRequest("name", null);
        CategoryResponse categoryResponse = createCategory(createCategoryRequest, registerCookie).getBody();
        List<Integer> categories = new ArrayList<>();
        categories.add(categoryResponse.getId());
        ProductRequest productRequest = new ProductRequest("product", 11, null, categories);
        ProductResponse productResponse = createProduct(productRequest, registerCookie).getBody();

        assertEquals(categoryResponse.getId(), (int) productResponse.getCategories().get(0));
        template.exchange(url + categoryResponse.getId(), HttpMethod.DELETE, new HttpEntity<>(null, headers), EmptyResponse.class);
        ProductResponse getResponse = template.exchange("http://localhost:" + port + "/api/products/" + productResponse.getId(), HttpMethod.GET, new HttpEntity<>(null, headers), ProductResponse.class).getBody();
        assertEquals(0, getResponse.getCategories().size());
    }

    @Test
    public void testDeleteCategoryWithChildContainsProduct() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);
        CategoryRequest createParent = new CategoryRequest("parent", null);
        CategoryResponse parent = createCategory(createParent, registerCookie).getBody();
        CategoryRequest createChild = new CategoryRequest("child", parent.getId());
        CategoryResponse child = createCategory(createChild, registerCookie).getBody();
        List<Integer> categories = new ArrayList<>();
        categories.add(child.getId());
        ProductRequest productRequest = new ProductRequest("product", 11, null, categories);
        ProductResponse productResponse = createProduct(productRequest, registerCookie).getBody();

        assertEquals(child.getId(), (int) productResponse.getCategories().get(0));
        template.exchange(url + parent.getId(), HttpMethod.DELETE, new HttpEntity<>(null, headers), EmptyResponse.class);
        ProductResponse getResponse = template.exchange("http://localhost:" + port + "/api/products/" + productResponse.getId(), HttpMethod.GET, new HttpEntity<>(null, headers), ProductResponse.class).getBody();
        assertEquals(0, getResponse.getCategories().size());
    }

    @Test
    public void testDeleteNotExistingCategory() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);
        try {
            template.exchange(url + 3333, HttpMethod.DELETE, new HttpEntity<>(null, headers), EmptyResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.CATEGORY_NOT_EXISTS);
        }
    }

    @Test
    public void testDeleteCategoryWithoutSession() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);
        CategoryRequest createRequest = new CategoryRequest("name", null);
        CategoryResponse response = createCategory(createRequest, registerCookie).getBody();
        logoutUser(registerCookie);
        try {
            template.exchange(url + response.getId(), HttpMethod.DELETE, new HttpEntity<>(null, headers), EmptyResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }

    @Test
    public void testDeleteCategoryNotAdmin() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));
        CategoryRequest createRequest = new CategoryRequest("name", null);
        CategoryResponse response = createCategory(createRequest, adminCookie).getBody();
        logoutUser(adminCookie);

        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "NotAdmin", "123321qq");
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
}