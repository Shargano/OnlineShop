package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.request.LoginRequest;
import net.thumbtack.onlineshop.dto.response.account.AccountInfoResponse;
import net.thumbtack.onlineshop.dto.response.account.AdminAccountInfoResponse;
import net.thumbtack.onlineshop.dto.response.account.ClientAccountInfoResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.ValidationErrorCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LoginAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/sessions";

    private void testInvalidRequest(LoginRequest request, ValidationErrorCode code) {
        try {
            template.exchange(url, HttpMethod.POST, new HttpEntity<>(request), AccountInfoResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            assertNull(getCookie(exc));
            handleHttpClientException(exc, code);
        }
    }

    @Test
    public void loginClient() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SayHello", "123321qq");
        HttpEntity<ClientAccountInfoResponse> entity = createClient(createClientRequest);
        String registerCookie = getCookie(entity);

        LoginRequest loginRequest = new LoginRequest("SayHello", "123321qq");
        ResponseEntity<ClientAccountInfoResponse> loginResponseEntity = template.exchange(url, HttpMethod.POST, new HttpEntity<>(loginRequest), ClientAccountInfoResponse.class);
        ClientAccountInfoResponse response = loginResponseEntity.getBody();
        String loginCookie = getCookie(loginResponseEntity);

        assertNotNull(loginCookie);
        assertNotEquals(registerCookie, loginCookie);

        assertNotNull(response.getId());
        assertEquals(createClientRequest.getFirstName(), response.getFirstName());
        assertEquals(createClientRequest.getLastName(), response.getLastName());
        assertEquals(createClientRequest.getPatronymic(), response.getPatronymic());
        assertEquals(createClientRequest.getEmail(), response.getEmail());
        assertEquals(createClientRequest.getAddress(), response.getAddress());
        assertEquals(createClientRequest.getPhone(), response.getPhone());
        assertEquals(0, response.getDeposit().getValue());
    }

    @Test
    public void loginAdmin() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));

        LoginRequest loginRequest = new LoginRequest("SayHello", "123321qq");
        ResponseEntity<AdminAccountInfoResponse> loginResponseEntity = template.exchange(url, HttpMethod.POST, new HttpEntity<>(loginRequest), AdminAccountInfoResponse.class);
        AdminAccountInfoResponse response = loginResponseEntity.getBody();
        String loginCookie = getCookie(loginResponseEntity);

        assertNotNull(loginCookie);
        assertNotEquals(registerCookie, loginCookie);

        assertNotNull(response.getId());
        assertEquals(createAdminRequest.getFirstName(), response.getFirstName());
        assertEquals(createAdminRequest.getLastName(), response.getLastName());
        assertEquals(createAdminRequest.getPatronymic(), response.getPatronymic());
        assertEquals(createAdminRequest.getPosition(), response.getPosition());
    }

    @Test
    public void loginNonExistingUser() {
        LoginRequest loginRequest = new LoginRequest("someLogin", "123321qq");
        try {
            template.exchange(url, HttpMethod.POST, new HttpEntity<>(loginRequest), AccountInfoResponse.class);
        } catch (HttpClientErrorException exc) {
            assertNull(getCookie(exc));
            handleHttpClientException(exc, OnlineShopErrorCode.USER_NOT_EXISTS);
        }
    }


    @Test()
    public void createUserWithInvalidLogin() {
        LoginRequest request = new LoginRequest("&&*#^: ", "123321qq");
        testInvalidRequest(request, ValidationErrorCode.LOGIN);
        request.setLogin("");
        testInvalidRequest(request, ValidationErrorCode.LOGIN);
        request.setLogin("     ");
        testInvalidRequest(request, ValidationErrorCode.LOGIN);
        request.setLogin(null);
        testInvalidRequest(request, ValidationErrorCode.LOGIN);
        request.setLogin("It is Very very long string, don't now, what can I write here, just some letters");
        testInvalidRequest(request, ValidationErrorCode.LOGIN);
    }

    @Test()
    public void createUserWithInvalidPassword() {
        LoginRequest request = new LoginRequest("SayHello", "");
        testInvalidRequest(request, ValidationErrorCode.PASSWORD);
        request.setPassword("     ");
        testInvalidRequest(request, ValidationErrorCode.PASSWORD);
        request.setPassword(null);
        testInvalidRequest(request, ValidationErrorCode.PASSWORD);
        request.setPassword("12321");
        testInvalidRequest(request, ValidationErrorCode.PASSWORD);
        request.setPassword("It is Very very long string, don't now, what can I write here, just some letters");
        testInvalidRequest(request, ValidationErrorCode.PASSWORD);
    }
}