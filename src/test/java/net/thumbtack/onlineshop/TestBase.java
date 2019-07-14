package net.thumbtack.onlineshop;

import com.google.gson.Gson;
import net.thumbtack.onlineshop.dto.request.CategoryRequest;
import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.request.ProductRequest;
import net.thumbtack.onlineshop.dto.response.*;
import net.thumbtack.onlineshop.dto.response.account.AdminAccountInfoResponse;
import net.thumbtack.onlineshop.dto.response.account.ClientAccountInfoResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.ValidationErrorCode;
import org.junit.Before;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;

public class TestBase {
    protected int port = 8888;
    private Gson gson = new Gson();
    private RestTemplate template = new RestTemplate();

    @Before
    public void clearDB() {
        template.exchange("http://localhost:" + port + "/api/debug/clear", HttpMethod.POST, new HttpEntity<>(null, null), EmptyResponse.class);
    }

    protected void logoutUser(String cookie) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        template.exchange("http://localhost:" + port + "/api/sessions", HttpMethod.DELETE, new HttpEntity<>(null, headers), EmptyResponse.class);
    }

    protected ResponseEntity<AdminAccountInfoResponse> createAdmin(CreateAdminRequest request) {
        ResponseEntity<AdminAccountInfoResponse> responseEntity = template.exchange("http://localhost:" + port + "/api/admins", HttpMethod.POST, new HttpEntity<>(request), AdminAccountInfoResponse.class);
        assertNotNull(getCookie(responseEntity));
        return responseEntity;
    }

    protected ResponseEntity<ClientAccountInfoResponse> createClient(CreateClientRequest request) {
        ResponseEntity<ClientAccountInfoResponse> responseEntity = template.exchange("http://localhost:" + port + "/api/clients", HttpMethod.POST, new HttpEntity<>(request), ClientAccountInfoResponse.class);
        assertNotNull(getCookie(responseEntity));
        return responseEntity;
    }

    protected ResponseEntity<CategoryResponse> createCategory(CategoryRequest request, String cookie) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, cookie);
        return template.exchange("http://localhost:" + port + "/api/categories", HttpMethod.POST, new HttpEntity<>(request, headers), CategoryResponse.class);
    }

    protected ResponseEntity<ProductResponse> createProduct(ProductRequest request, String cookie) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, cookie);
        return template.exchange("http://localhost:" + port + "/api/products", HttpMethod.POST, new HttpEntity<>(request, headers), ProductResponse.class);
    }

    protected ErrorItem getErrorResponse(HttpStatusCodeException exception) {
        return gson.fromJson(exception.getResponseBodyAsString(), ErrorResponse.class).getErrors().get(0);
    }

    protected String getCookie(HttpStatusCodeException exception) {
        return exception.getResponseHeaders().getFirst(HttpHeaders.SET_COOKIE);
    }

    protected String getCookie(HttpEntity entity) {
        return entity.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
    }

    protected void checkErrorResponse(ErrorItem response, ValidationErrorCode code) {
        assertEquals(code, ValidationErrorCode.valueOf(response.getErrorCode()));
        assertTrue(response.getField().equalsIgnoreCase(code.getField()));
    }

    protected void checkErrorResponse(ErrorItem response, OnlineShopErrorCode code) {
        assertEquals(code, OnlineShopErrorCode.valueOf(response.getErrorCode()));
        assertTrue(response.getField().equalsIgnoreCase(code.getField()));
    }

    protected void handleHttpClientException(HttpClientErrorException exc, ValidationErrorCode code) {
        ErrorItem response = getErrorResponse(exc);
        assertEquals(400, exc.getStatusCode().value());
        checkErrorResponse(response, code);
    }

    protected void handleHttpClientException(HttpClientErrorException exc, OnlineShopErrorCode code) {
        ErrorItem response = getErrorResponse(exc);
        assertEquals(400, exc.getStatusCode().value());
        checkErrorResponse(response, code);
    }

}