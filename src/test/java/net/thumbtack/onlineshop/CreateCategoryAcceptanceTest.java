package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.CategoryRequest;
import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.response.CategoryResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.ValidationErrorCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CreateCategoryAcceptanceTest extends TestBase {

    private void testInvalidRequest(CategoryRequest request, String cookie, ValidationErrorCode code) {
        try {
            createCategory(request, cookie);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, code);
        }
    }

    @Test
    public void testCreateCategory() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));

        CategoryRequest createRequest = new CategoryRequest("name", null);
        CategoryResponse response = createCategory(createRequest, registerCookie).getBody();

        assertNotEquals(0, response.getId());
        assertEquals(createRequest.getName(), response.getName());
        assertNull(response.getParentId());
        assertNull(response.getParentName());
    }

    @Test
    public void testCreateSubCategory() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));

        CategoryRequest createParent = new CategoryRequest("parent", null);
        CategoryResponse parent = createCategory(createParent, registerCookie).getBody();
        CategoryRequest createChild = new CategoryRequest("child", parent.getId());
        CategoryResponse child = createCategory(createChild, registerCookie).getBody();

        assertNotEquals(0, child.getId());
        assertEquals(createChild.getName(), child.getName());
        assertEquals(parent.getId(), (int) child.getParentId());
        assertEquals(parent.getName(), child.getParentName());
    }

    @Test
    public void testCreateCategoryWithoutSession() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        CategoryRequest createRequest = new CategoryRequest("name", null);
        logoutUser(registerCookie);
        try {
            createCategory(createRequest, registerCookie);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }

    @Test
    public void testClientCreateCategory() {
        CreateClientRequest request = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SayHello", "123321qq");
        String registerCookie = getCookie(createClient(request));
        CategoryRequest createRequest = new CategoryRequest("name", null);
        try {
            createCategory(createRequest, registerCookie);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.USER_NOT_ADMIN);
        }
    }

    @Test
    public void createAlreadyExistingCategory() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        CategoryRequest createRequest = new CategoryRequest("name", null);
        createCategory(createRequest, registerCookie).getBody();
        try {
            createCategory(createRequest, registerCookie);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.CATEGORY_ALREADY_EXISTS);
        }
    }

    @Test
    public void createSubCategoryWithNotExistingParent() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        CategoryRequest createRequest = new CategoryRequest("name", 12321);
        try {
            createCategory(createRequest, registerCookie);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.CATEGORY_NOT_EXISTS);
        }
    }

    @Test()
    public void testCreateCategoryWithInvalidName() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        CategoryRequest request = new CategoryRequest("", null);
        testInvalidRequest(request, registerCookie, ValidationErrorCode.NAME);
        request.setName("       ");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.NAME);
        request.setName(null);
        testInvalidRequest(request, registerCookie, ValidationErrorCode.NAME);
    }

}