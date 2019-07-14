package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.CategoryRequest;
import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.response.CategoryResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GetAllCategoriesAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/categories";

    @Test
    public void testGetAllCategories() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);

        CategoryResponse abc = createCategory(new CategoryRequest("abc", null), registerCookie).getBody();
        CategoryResponse category = createCategory(new CategoryRequest("category", null), registerCookie).getBody();
        CategoryResponse best = createCategory(new CategoryRequest("best", null), registerCookie).getBody();
        CategoryResponse of = createCategory(new CategoryRequest("of", null), registerCookie).getBody();
        CategoryResponse apples = createCategory(new CategoryRequest("Apples", null), registerCookie).getBody();

        ResponseEntity<List<CategoryResponse>> responseEntity = template.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<List<CategoryResponse>>() {
        });
        List<CategoryResponse> responses = responseEntity.getBody();

        assertEquals(5, responses.size());
        assertEquals(abc.getName(), responses.get(0).getName());
        assertEquals(abc.getId(), responses.get(0).getId());
        assertEquals(apples.getName(), responses.get(1).getName());
        assertEquals(apples.getId(), responses.get(1).getId());
        assertEquals(best.getName(), responses.get(2).getName());
        assertEquals(best.getId(), responses.get(2).getId());
        assertEquals(category.getName(), responses.get(3).getName());
        assertEquals(category.getId(), responses.get(3).getId());
        assertEquals(of.getName(), responses.get(4).getName());
        assertEquals(of.getId(), responses.get(4).getId());
    }

    @Test
    public void testGetAllCategoriesWithSubcategories() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);

        CategoryResponse abc = createCategory(new CategoryRequest("abc", null), registerCookie).getBody();
        CategoryResponse category = createCategory(new CategoryRequest("category", null), registerCookie).getBody();
        CategoryResponse best = createCategory(new CategoryRequest("best", null), registerCookie).getBody();
        CategoryResponse subCategoryA = createCategory(new CategoryRequest("subCategoryA", abc.getId()), registerCookie).getBody();
        CategoryResponse childA = createCategory(new CategoryRequest("ChildA", abc.getId()), registerCookie).getBody();
        CategoryResponse subCategoryB = createCategory(new CategoryRequest("subCategoryB", best.getId()), registerCookie).getBody();
        CategoryResponse childB = createCategory(new CategoryRequest("childB", best.getId()), registerCookie).getBody();


        ResponseEntity<List<CategoryResponse>> responseEntity = template.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<List<CategoryResponse>>() {
        });
        List<CategoryResponse> responses = responseEntity.getBody();

        assertEquals(7, responses.size());
        assertEquals(abc, responses.get(0));
        assertEquals(childA, responses.get(1));
        assertEquals(subCategoryA, responses.get(2));
        assertEquals(best, responses.get(3));
        assertEquals(childB, responses.get(4));
        assertEquals(subCategoryB, responses.get(5));
        assertEquals(category, responses.get(6));
    }

    @Test
    public void testGetAllCategoriesWithoutSession() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);
        createCategory(new CategoryRequest("abc", null), registerCookie).getBody();

        logoutUser(registerCookie);
        try {
            template.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<List<CategoryResponse>>() {
            });
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }

    @Test
    public void testGetAllCategoriesNotAdmin() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String adminCookie = getCookie(createAdmin(createAdminRequest));
        createCategory(new CategoryRequest("abc", null), adminCookie).getBody();
        logoutUser(adminCookie);
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "NotAdmin", "123321qq");
        String clientCookie = getCookie(createClient(createClientRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, clientCookie);
        try {
            template.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<List<CategoryResponse>>() {
            });
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.USER_NOT_ADMIN);
        }
    }
}