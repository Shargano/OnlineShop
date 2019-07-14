package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.CategoryRequest;
import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.request.ProductRequest;
import net.thumbtack.onlineshop.dto.response.CategoryResponse;
import net.thumbtack.onlineshop.dto.response.ProductResponse;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GetAllProductsAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/products";

    @Test
    public void testGetProductsNotExistingCategory() {
        CreateAdminRequest request = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String cookie = getCookie(createAdmin(request));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("order", "product")
                .queryParam("category", 0);
        List<ProductResponse> responses = template.exchange(builder.build().toUriString(), HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<List<ProductResponse>>() {
        }).getBody();

        assertEquals(0, responses.size());
    }

    @Test
    public void testGetProductsWithoutSession() {
        CreateAdminRequest request = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String cookie = getCookie(createAdmin(request));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);

        logoutUser(cookie);
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam("order", "product");
            template.exchange(builder.build().toUriString(), HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<List<ProductResponse>>() {
            });
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }

    @Test
    public void testGetProductsOrderByProductAllCategories() {
        CreateAdminRequest request = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String cookie = getCookie(createAdmin(request));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);

        ProductResponse books = createProduct(new ProductRequest("books", 12345, 100, null), cookie).getBody();
        ProductResponse apple = createProduct(new ProductRequest("apple", 12345, 100, null), cookie).getBody();
        CategoryResponse vevoCategory = createCategory(new CategoryRequest("Vevo", null), cookie).getBody();
        CategoryResponse carsCategory = createCategory(new CategoryRequest("Cars", null), cookie).getBody();
        List<Integer> categories1 = new ArrayList<>();
        categories1.add(vevoCategory.getId());
        ProductResponse cheese = createProduct(new ProductRequest("cheese", 12345, 100, categories1), cookie).getBody();
        ProductResponse zero = createProduct(new ProductRequest("zero", 12345, 100, categories1), cookie).getBody();
        List<Integer> categories2 = new ArrayList<>(categories1);
        categories2.add(carsCategory.getId());
        ProductResponse audi = createProduct(new ProductRequest("audi", 12345, 100, categories2), cookie).getBody();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("order", "product");
        ResponseEntity<List<ProductResponse>> responseEntity = template.exchange(builder.build().toUriString(), HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<List<ProductResponse>>() {
        });
        List<ProductResponse> responses = responseEntity.getBody();

        assertEquals(5, responses.size());
        assertEquals(apple, responses.get(0));
        assertEquals(audi, responses.get(1));
        assertEquals(books, responses.get(2));
        assertEquals(cheese, responses.get(3));
        assertEquals(zero, responses.get(4));
    }

    @Test
    public void testGetProductsOrderByProductNullCategories() {
        CreateAdminRequest request = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String cookie = getCookie(createAdmin(request));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);

        ProductResponse books = createProduct(new ProductRequest("books", 12345, 0, null), cookie).getBody();
        ProductResponse apple = createProduct(new ProductRequest("apple", 12345, 0, null), cookie).getBody();
        CategoryResponse category1 = createCategory(new CategoryRequest("Vevo", null), cookie).getBody();
        CategoryResponse category2 = createCategory(new CategoryRequest("Cars", null), cookie).getBody();
        List<Integer> categories1 = new ArrayList<>();
        categories1.add(category1.getId());
        ProductResponse cheese = createProduct(new ProductRequest("cheese", 12345, 0, categories1), cookie).getBody();
        ProductResponse zero = createProduct(new ProductRequest("zero", 12345, 0, categories1), cookie).getBody();
        List<Integer> categories2 = new ArrayList<>(categories1);
        categories2.add(category2.getId());
        ProductResponse audi = createProduct(new ProductRequest("audi", 12345, 0, categories2), cookie).getBody();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("order", "product")
                .queryParam("category", "");
        ResponseEntity<List<ProductResponse>> responseEntity = template.exchange(builder.build().toUri(), HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<List<ProductResponse>>() {
        });
        List<ProductResponse> responses = responseEntity.getBody();

        assertEquals(2, responses.size());
        assertEquals(apple, responses.get(0));
        assertEquals(books, responses.get(1));
    }

    @Test
    public void testGetProductsOrderByProductCategoriesNumbers() {
        CreateAdminRequest request = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String cookie = getCookie(createAdmin(request));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);

        ProductResponse books = createProduct(new ProductRequest("books", 12345, 0, null), cookie).getBody();
        ProductResponse apple = createProduct(new ProductRequest("apple", 12345, 0, null), cookie).getBody();
        CategoryResponse category1 = createCategory(new CategoryRequest("Vevo", null), cookie).getBody();
        CategoryResponse category2 = createCategory(new CategoryRequest("Cars", null), cookie).getBody();
        List<Integer> categories1 = new ArrayList<>();
        categories1.add(category1.getId());
        ProductResponse cheese = createProduct(new ProductRequest("cheese", 12345, 0, categories1), cookie).getBody();
        ProductResponse zero = createProduct(new ProductRequest("zero", 12345, 0, categories1), cookie).getBody();
        List<Integer> categories2 = new ArrayList<>(categories1);
        categories2.add(category2.getId());
        ProductResponse audi = createProduct(new ProductRequest("audi", 12345, 0, categories2), cookie).getBody();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("order", "product")
                .queryParam("category", category1.getId(), category2.getId());
        ResponseEntity<List<ProductResponse>> responseEntity = template.exchange(builder.build().toUri(), HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<List<ProductResponse>>() {
        });
        List<ProductResponse> responses = responseEntity.getBody();

        assertEquals(3, responses.size());
        assertEquals(audi, responses.get(0));
        assertEquals(cheese, responses.get(1));
        assertEquals(zero, responses.get(2));
    }

    @Test
    public void testGetProductsOrderByCategoryAllCategories() {
        CreateAdminRequest request = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String cookie = getCookie(createAdmin(request));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);

        ProductResponse books = createProduct(new ProductRequest("books", 12345, 0, null), cookie).getBody();
        ProductResponse apple = createProduct(new ProductRequest("apple", 12345, 0, null), cookie).getBody();
        CategoryResponse category1 = createCategory(new CategoryRequest("Vevo", null), cookie).getBody();
        CategoryResponse category2 = createCategory(new CategoryRequest("Cars", null), cookie).getBody();
        List<Integer> categories1 = new ArrayList<>();
        categories1.add(category1.getId());
        ProductResponse cheese = createProduct(new ProductRequest("cheese", 12345, 0, categories1), cookie).getBody();
        ProductResponse zero = createProduct(new ProductRequest("zero", 12345, 0, categories1), cookie).getBody();
        List<Integer> categories2 = new ArrayList<>(categories1);
        categories2.add(category2.getId());
        ProductResponse audi = createProduct(new ProductRequest("audi", 12345, 0, categories2), cookie).getBody();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("order", "category");
        ResponseEntity<List<ProductResponse>> responseEntity = template.exchange(builder.build().toUriString(), HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<List<ProductResponse>>() {
        });
        List<ProductResponse> responses = responseEntity.getBody();

        assertEquals(6, responses.size());
        checkProductFields(apple, responses.get(0));
        assertEquals(0, responses.get(0).getCategories().size());
        checkProductFields(books, responses.get(1));
        assertEquals(0, responses.get(1).getCategories().size());
        checkProductFields(audi, responses.get(2));
        checkProductCategory(category2, responses.get(2).getCategories());
        checkProductFields(audi, responses.get(3));
        checkProductCategory(category1, responses.get(3).getCategories());
        checkProductFields(cheese, responses.get(4));
        checkProductCategory(category1, responses.get(4).getCategories());
        checkProductFields(zero, responses.get(5));
        checkProductCategory(category1, responses.get(5).getCategories());
    }

    @Test
    public void testGetProductsOrderByCategoryNullCategories() {
        CreateAdminRequest request = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String cookie = getCookie(createAdmin(request));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);

        ProductResponse books = createProduct(new ProductRequest("books", 12345, 0, null), cookie).getBody();
        ProductResponse apple = createProduct(new ProductRequest("apple", 12345, 0, null), cookie).getBody();
        CategoryResponse category1 = createCategory(new CategoryRequest("Vevo", null), cookie).getBody();
        CategoryResponse category2 = createCategory(new CategoryRequest("Cars", null), cookie).getBody();
        List<Integer> categories1 = new ArrayList<>();
        categories1.add(category1.getId());
        ProductResponse cheese = createProduct(new ProductRequest("cheese", 12345, 0, categories1), cookie).getBody();
        ProductResponse zero = createProduct(new ProductRequest("zero", 12345, 0, categories1), cookie).getBody();
        List<Integer> categories2 = new ArrayList<>(categories1);
        categories2.add(category2.getId());
        ProductResponse audi = createProduct(new ProductRequest("audi", 12345, 0, categories2), cookie).getBody();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("order", "category")
                .queryParam("category", "");
        ResponseEntity<List<ProductResponse>> responseEntity = template.exchange(builder.build().toUriString(), HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<List<ProductResponse>>() {
        });
        List<ProductResponse> responses = responseEntity.getBody();

        assertEquals(2, responses.size());
        checkProductFields(apple, responses.get(0));
        assertEquals(0, responses.get(0).getCategories().size());
        checkProductFields(books, responses.get(1));
        assertEquals(0, responses.get(1).getCategories().size());
    }

    @Test
    public void testGetProductsOrderByCategoryCategoriesNumbers() {
        CreateAdminRequest request = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String cookie = getCookie(createAdmin(request));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);

        ProductResponse books = createProduct(new ProductRequest("books", 12345, 0, null), cookie).getBody();
        ProductResponse apple = createProduct(new ProductRequest("apple", 12345, 0, null), cookie).getBody();
        CategoryResponse category1 = createCategory(new CategoryRequest("Vevo", null), cookie).getBody();
        CategoryResponse category2 = createCategory(new CategoryRequest("Cars", null), cookie).getBody();
        List<Integer> categories1 = new ArrayList<>();
        categories1.add(category1.getId());
        ProductResponse cheese = createProduct(new ProductRequest("cheese", 12345, 0, categories1), cookie).getBody();
        ProductResponse zero = createProduct(new ProductRequest("zero", 12345, 0, categories1), cookie).getBody();
        List<Integer> categories2 = new ArrayList<>(categories1);
        categories2.add(category2.getId());
        ProductResponse audi = createProduct(new ProductRequest("audi", 12345, 0, categories2), cookie).getBody();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("order", "category")
                .queryParam("category", category1.getId(), category2.getId());
        ResponseEntity<List<ProductResponse>> responseEntity = template.exchange(builder.build().toUriString(), HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<List<ProductResponse>>() {
        });
        List<ProductResponse> responses = responseEntity.getBody();

        assertEquals(4, responses.size());
        checkProductFields(audi, responses.get(0));
        checkProductCategory(category2, responses.get(0).getCategories());
        checkProductFields(audi, responses.get(1));
        checkProductCategory(category1, responses.get(1).getCategories());
        checkProductFields(cheese, responses.get(2));
        checkProductCategory(category1, responses.get(2).getCategories());
        checkProductFields(zero, responses.get(3));
        checkProductCategory(category1, responses.get(3).getCategories());
    }

    private void checkProductFields(ProductResponse expected, ProductResponse actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getCount(), actual.getCount());
        assertEquals(expected.getPrice(), actual.getPrice());
    }

    private void checkProductCategory(CategoryResponse expected, List<Integer> actual) {
        assertEquals(1, actual.size());
        assertEquals(expected.getId(), (int) actual.get(0));
    }

}