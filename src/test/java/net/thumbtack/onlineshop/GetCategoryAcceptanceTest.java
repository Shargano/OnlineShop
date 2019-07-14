package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.CategoryRequest;
import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GetCategoryAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/categories/";

    @Test
    public void testGetCategory() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);

        CategoryRequest createRequest = new CategoryRequest("name", null);
        CategoryResponse createResponse = createCategory(createRequest, registerCookie).getBody();
        CategoryResponse getResponse = template.exchange(url + createResponse.getId(), HttpMethod.GET, new HttpEntity<>(null, headers), CategoryResponse.class).getBody();

        assertEquals(createRequest.getName(), getResponse.getName());
        assertEquals(createRequest.getParentId(), getResponse.getParentId());
        assertEquals(createResponse.getParentName(), getResponse.getParentName());
    }

    @Test
    public void testGetSubCategory() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);

        CategoryRequest parentRequest = new CategoryRequest("category", null);
        CategoryResponse parentResponse = createCategory(parentRequest, registerCookie).getBody();
        CategoryRequest childRequest = new CategoryRequest("subcategory", parentResponse.getId());
        CategoryResponse childResponse = createCategory(childRequest, registerCookie).getBody();
        CategoryResponse getResponse = template.exchange(url + childResponse.getId(), HttpMethod.GET, new HttpEntity<>(null, headers), CategoryResponse.class).getBody();

        assertEquals(childResponse.getName(), getResponse.getName());
        assertEquals(parentResponse.getId(), (int) getResponse.getParentId());
        assertEquals(parentResponse.getName(), getResponse.getParentName());
    }

    @Test
    public void testGetNonExistingCategory() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);

        try {
            template.exchange(url + 123, HttpMethod.GET, new HttpEntity<>(null, headers), CategoryResponse.class);
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.CATEGORY_NOT_EXISTS);
        }
    }

    @Test
    public void testClientGetCategory() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));

        CategoryRequest createRequest = new CategoryRequest("category", null);
        CategoryResponse createResponse = createCategory(createRequest, adminCookie).getBody();

        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "BadClient", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, clientCookie);
        try {
            template.exchange(url + createResponse.getId(), HttpMethod.GET, new HttpEntity<>(null, headers), CategoryResponse.class);
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.USER_NOT_ADMIN);
        }
    }

    @Test
    public void testGetCategoryWithoutSession() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);

        CategoryRequest createRequest = new CategoryRequest("category", null);
        CategoryResponse createResponse = createCategory(createRequest, registerCookie).getBody();

        logoutUser(registerCookie);
        try {
            template.exchange(url + createResponse.getId(), HttpMethod.GET, new HttpEntity<>(null, headers), CategoryResponse.class);
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }
}