package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.CategoryRequest;
import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.request.UpdateCategoryRequest;
import net.thumbtack.onlineshop.dto.response.CategoryResponse;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UpdateCategoryAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/categories/";

    @Test
    public void testUpdateCategory() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);

        CategoryRequest createRequest = new CategoryRequest("name", null);
        CategoryResponse createResponse = createCategory(createRequest, registerCookie).getBody();

        UpdateCategoryRequest updateRequest = new UpdateCategoryRequest("SuperNewUnusualName");
        template.exchange(url + createResponse.getId(), HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), CategoryResponse.class);
        CategoryResponse getResponse = template.exchange(url + createResponse.getId(), HttpMethod.GET, new HttpEntity<>(null, headers), CategoryResponse.class).getBody();
        assertEquals(createResponse.getId(), getResponse.getId());
        assertEquals(updateRequest.getName(), getResponse.getName());
        assertEquals(createRequest.getParentId(), getResponse.getParentId());
        assertEquals(createResponse.getParentName(), getResponse.getParentName());
    }

    @Test
    public void testUpdateSubCategory() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);

        CategoryRequest parentRequest = new CategoryRequest("parent", null);
        CategoryResponse parentResponse = createCategory(parentRequest, registerCookie).getBody();
        CategoryRequest newParentRequest = new CategoryRequest("newParent", null);
        CategoryResponse newParentResponse = createCategory(newParentRequest, registerCookie).getBody();
        CategoryRequest childRequest = new CategoryRequest("subcategory", parentResponse.getId());
        CategoryResponse childResponse = createCategory(childRequest, registerCookie).getBody();

        assertEquals(childRequest.getName(), childResponse.getName());
        assertEquals(parentResponse.getId(), (int) childResponse.getParentId());
        assertEquals(parentResponse.getName(), childResponse.getParentName());

        UpdateCategoryRequest updateRequest = new UpdateCategoryRequest(null, newParentResponse.getId());
        template.exchange(url + childResponse.getId(), HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), CategoryResponse.class);
        CategoryResponse getResponse = template.exchange(url + childResponse.getId(), HttpMethod.GET, new HttpEntity<>(null, headers), CategoryResponse.class).getBody();

        assertEquals(childResponse.getId(), getResponse.getId());
        assertEquals(childRequest.getName(), getResponse.getName());
        assertEquals(newParentResponse.getId(), (int) getResponse.getParentId());
        assertEquals(newParentResponse.getName(), getResponse.getParentName());
    }

    @Test
    public void testUpdateCategorySetParent() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);

        CategoryRequest createRequest = new CategoryRequest("name", null);
        CategoryResponse createResponse = createCategory(createRequest, registerCookie).getBody();
        CategoryRequest parentRequest = new CategoryRequest("parent", null);
        CategoryResponse createParentResponse = createCategory(parentRequest, registerCookie).getBody();

        UpdateCategoryRequest updateRequest = new UpdateCategoryRequest("SuperNewUnusualName", createParentResponse.getId());
        try {
            template.exchange(url + createResponse.getId(), HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), CategoryResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.CATEGORY_PARENT);
        }
    }

    @Test
    public void testUpdateSubCategorySetNullParent() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);

        CategoryRequest parentRequest = new CategoryRequest("parent", null);
        CategoryResponse parentResponse = createCategory(parentRequest, registerCookie).getBody();
        CategoryRequest childRequest = new CategoryRequest("subcategory", parentResponse.getId());
        CategoryResponse childResponse = createCategory(childRequest, registerCookie).getBody();

        UpdateCategoryRequest updateRequest = new UpdateCategoryRequest("SuperNewUnusualName", null);
        try {
            template.exchange(url + childResponse.getId(), HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), CategoryResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.CATEGORY_PARENT);
        }
    }

    @Test
    public void testUpdateSubCategorySetNotExistingParent() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);

        CategoryRequest parentRequest = new CategoryRequest("parent", null);
        CategoryResponse parentResponse = createCategory(parentRequest, registerCookie).getBody();
        CategoryRequest childRequest = new CategoryRequest("subcategory", parentResponse.getId());
        CategoryResponse childResponse = createCategory(childRequest, registerCookie).getBody();

        UpdateCategoryRequest updateRequest = new UpdateCategoryRequest("SuperNewUnusualName", 3333);
        try {
            template.exchange(url + childResponse.getId(), HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), CategoryResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.CATEGORY_NOT_EXISTS);
        }
    }

    @Test
    public void testUpdateCategoryNullFields() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);

        CategoryRequest createRequest = new CategoryRequest("name", null);
        CategoryResponse createResponse = createCategory(createRequest, registerCookie).getBody();
        UpdateCategoryRequest updateRequest = new UpdateCategoryRequest();
        try {
            template.exchange(url + createResponse.getId(), HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), CategoryResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.CATEGORY_FIELDS);
        }
    }

    @Test
    public void testUpdateCategoryNotAdmin() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));
        CategoryRequest createRequest = new CategoryRequest("name", null);
        CategoryResponse createResponse = createCategory(createRequest, adminCookie).getBody();

        UpdateCategoryRequest updateRequest = new UpdateCategoryRequest("SuperNewUnusualName", null);
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "NotAdmin", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, clientCookie);
        try {
            template.exchange(url + createResponse.getId(), HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), CategoryResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.USER_NOT_ADMIN);
        }
    }

    @Test
    public void testUpdateCategoryWithoutSession() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);

        CategoryRequest createRequest = new CategoryRequest("name", null);
        CategoryResponse createResponse = createCategory(createRequest, registerCookie).getBody();

        UpdateCategoryRequest updateRequest = new UpdateCategoryRequest("SuperNewUnusualName", null);
        logoutUser(registerCookie);
        try {
            template.exchange(url + createResponse.getId(), HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), CategoryResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }
}