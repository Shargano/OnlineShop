package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.CategoryRequest;
import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.request.ProductRequest;
import net.thumbtack.onlineshop.dto.response.ProductResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.ValidationErrorCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CreateProductAcceptanceTest extends TestBase {

    private void testInvalidRequest(ProductRequest request, String cookie, ValidationErrorCode code) {
        try {
            createProduct(request, cookie);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, code);
        }
    }

    @Test
    public void testCreateProduct() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        ProductRequest createRequest = new ProductRequest("Name", 123, 0, null);
        ProductResponse response = createProduct(createRequest, registerCookie).getBody();

        assertNotNull(response.getId());
        assertEquals(createRequest.getName(), response.getName());
        assertEquals(createRequest.getPrice(), response.getPrice());
        assertEquals((int) createRequest.getCount(), response.getCount());
        assertEquals(0, response.getCategories().size());
    }

    @Test
    public void testCreateProductDefaultCount() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));

        ProductRequest createRequest = new ProductRequest("Name", 123, null, null);
        ProductResponse response = createProduct(createRequest, registerCookie).getBody();

        assertNotNull(response.getId());
        assertEquals(createRequest.getName(), response.getName());
        assertEquals(createRequest.getPrice(), response.getPrice());
        assertEquals(0, response.getCount());
        assertEquals(0, response.getCategories().size());
    }

    @Test
    public void testCreateProductsWithSameNames() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));

        ProductRequest createRequest = new ProductRequest("Name", 123, 0, null);
        ProductResponse firstResponse = createProduct(createRequest, registerCookie).getBody();
        ProductResponse secondResponse = createProduct(createRequest, registerCookie).getBody();

        assertNotEquals(firstResponse.getId(), secondResponse.getId());
        assertEquals(firstResponse.getName(), secondResponse.getName());
        assertEquals(firstResponse.getPrice(), secondResponse.getPrice());
        assertEquals(firstResponse.getCount(), secondResponse.getCount());
        assertEquals(firstResponse.getCategories(), secondResponse.getCategories());
    }

    @Test
    public void testCreateProductWithCategory() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));

        CategoryRequest createCategoryRequest = new CategoryRequest("category", null);
        List<Integer> categories = new ArrayList<>();
        categories.add(createCategory(createCategoryRequest, registerCookie).getBody().getId());

        ProductRequest createRequest = new ProductRequest("Name", 123, 0, categories);
        ProductResponse response = createProduct(createRequest, registerCookie).getBody();

        assertNotNull(response.getId());
        assertEquals(createRequest.getName(), response.getName());
        assertEquals(createRequest.getPrice(), response.getPrice());
        assertEquals((int) createRequest.getCount(), response.getCount());
        assertEquals(1, response.getCategories().size());
        assertEquals(categories, response.getCategories());
    }

    @Test
    public void testCreateProductWithNotExistingCategory() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));

        List<Integer> categories = new ArrayList<>();
        categories.add(123234);
        ProductRequest createRequest = new ProductRequest("Name", 123, 0, categories);
        try {
            createProduct(createRequest, registerCookie);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.CATEGORY_NOT_EXISTS);
        }
    }

    @Test
    public void testCreateProductWithoutSession() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));

        ProductRequest createRequest = new ProductRequest("Name", 123, 0, null);
        logoutUser(registerCookie);
        try {
            createProduct(createRequest, registerCookie);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }

    @Test
    public void testCreateProductNotAdmin() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SayHello", "123321qq");
        String registerCookie = getCookie(createClient(createClientRequest));

        ProductRequest createRequest = new ProductRequest("Name", 123, 0, null);
        try {
            createProduct(createRequest, registerCookie);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.USER_NOT_ADMIN);
        }
    }

    @Test
    public void testCreateProductInvalidName() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String cookie = getCookie(createAdmin(createAdminRequest));
        ProductRequest request = new ProductRequest("", 123, 10, null);
        testInvalidRequest(request, cookie, ValidationErrorCode.NAME);
        request.setName("       ");
        testInvalidRequest(request, cookie, ValidationErrorCode.NAME);
        request.setName(null);
        testInvalidRequest(request, cookie, ValidationErrorCode.NAME);
    }

    @Test
    public void testCreateProductInvalidPrice() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String cookie = getCookie(createAdmin(createAdminRequest));
        ProductRequest request = new ProductRequest("some", -10, 10, null);
        testInvalidRequest(request, cookie, ValidationErrorCode.PRICE);
        request.setPrice(0);
        testInvalidRequest(request, cookie, ValidationErrorCode.PRICE);
    }


}