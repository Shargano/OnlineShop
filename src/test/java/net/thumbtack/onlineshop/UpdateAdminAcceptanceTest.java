package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.request.UpdateAdminRequest;
import net.thumbtack.onlineshop.dto.response.account.AdminAccountInfoResponse;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UpdateAdminAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/admins";

    private void testInvalidRequest(UpdateAdminRequest request, String cookie, ValidationErrorCode code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, cookie);
        try {
            template.exchange(url, HttpMethod.PUT, new HttpEntity<>(request, headers), AdminAccountInfoResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            assertNull(getCookie(exc));
            handleHttpClientException(exc, code);
        }
    }

    @Test
    public void testUpdateAdmin() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);
        UpdateAdminRequest updateAdminRequest = new UpdateAdminRequest("Мишко", "Дедун", null, "DevOps", "123321qq", "123321Google");

        template.exchange(url, HttpMethod.PUT, new HttpEntity<>(updateAdminRequest, headers), AdminAccountInfoResponse.class);
        AdminAccountInfoResponse getResponse = template.exchange("http://localhost:" + port + "/api/accounts", HttpMethod.GET, new HttpEntity<>(null, headers), AdminAccountInfoResponse.class).getBody();

        assertEquals(updateAdminRequest.getFirstName(), getResponse.getFirstName());
        assertEquals(updateAdminRequest.getLastName(), getResponse.getLastName());
        assertEquals(updateAdminRequest.getPatronymic(), getResponse.getPatronymic());
        assertEquals(updateAdminRequest.getPosition(), getResponse.getPosition());
    }

    @Test
    public void testClientUpdateAdmin() {
        CreateClientRequest request = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SayHello", "123321qq");
        String registerCookie = getCookie(createClient(request));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);
        UpdateAdminRequest updateAdminRequest = new UpdateAdminRequest("Мишко", "Дедун", null, "DevOps", "notPassword", "123321Google");
        try {
            template.exchange(url, HttpMethod.PUT, new HttpEntity<>(updateAdminRequest, headers), AdminAccountInfoResponse.class);
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.USER_NOT_ADMIN);
        }
    }

    @Test
    public void testUpdateAdminIncorrectPassword() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);
        UpdateAdminRequest updateAdminRequest = new UpdateAdminRequest("Мишко", "Дедун", null, "DevOps", "notPassword", "123321Google");
        try {
            template.exchange(url, HttpMethod.PUT, new HttpEntity<>(updateAdminRequest, headers), AdminAccountInfoResponse.class);
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.PASSWORD_FAILED);
        }
    }

    @Test
    public void testUpdateAdminNotLogged() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);
        UpdateAdminRequest updateAdminRequest = new UpdateAdminRequest("Мишко", "Дедун", null, "DevOps", "123321qq", "123321Google");

        logoutUser(registerCookie);
        try {
            template.exchange(url, HttpMethod.PUT, new HttpEntity<>(updateAdminRequest, headers), AdminAccountInfoResponse.class);
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }

    @Test()
    public void testUpdateAdminWithInvalidFirstName() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        UpdateAdminRequest request = new UpdateAdminRequest("Michael", "Дедун", null, "DevOps", "notPassword", "123321Google");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.FIRSTNAME);
        request.setFirstName("");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.FIRSTNAME);
        request.setFirstName("       ");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.FIRSTNAME);
        request.setFirstName(null);
        testInvalidRequest(request, registerCookie, ValidationErrorCode.FIRSTNAME);
        request.setFirstName("It is Very very long string, don't now, what can I write here, just some letters");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.FIRSTNAME);
    }

    @Test()
    public void testUpdateAdminWithInvalidLastName() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        UpdateAdminRequest request = new UpdateAdminRequest("Михаил", "Dedun", null, "DevOps", "notPassword", "123321Google");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.LASTNAME);
        request.setLastName("");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.LASTNAME);
        request.setLastName("       ");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.LASTNAME);
        request.setLastName(null);
        testInvalidRequest(request, registerCookie, ValidationErrorCode.LASTNAME);
        request.setLastName("It is Very very long string, don't now, what can I write here, just some letters");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.LASTNAME);
    }

    @Test()
    public void testUpdateAdminWithInvalidPosition() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        UpdateAdminRequest request = new UpdateAdminRequest("Михаил", "Дедун", null, "", "notPassword", "123321Google");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.POSITION);
        request.setPosition("       ");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.POSITION);
        request.setPosition(null);
        testInvalidRequest(request, registerCookie, ValidationErrorCode.POSITION);
    }

    @Test()
    public void testUpdateAdminWithInvalidOldPassword() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        UpdateAdminRequest request = new UpdateAdminRequest("Михаил", "Дедун", null, "DevOps", "", "123321Google");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.OLDPASSWORD);
        request.setOldPassword("     ");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.OLDPASSWORD);
        request.setOldPassword(null);
        testInvalidRequest(request, registerCookie, ValidationErrorCode.OLDPASSWORD);
        request.setOldPassword("12321");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.OLDPASSWORD);
        request.setOldPassword("It is Very very long string, don't now, what can I write here, just some letters");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.OLDPASSWORD);
    }

    @Test()
    public void testUpdateAdminWithInvalidNewPassword() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        UpdateAdminRequest request = new UpdateAdminRequest("Михаил", "Дедун", null, "DevOps", "123321qq", "");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.NEWPASSWORD);
        request.setNewPassword("     ");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.NEWPASSWORD);
        request.setNewPassword(null);
        testInvalidRequest(request, registerCookie, ValidationErrorCode.NEWPASSWORD);
        request.setNewPassword("12321");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.NEWPASSWORD);
        request.setNewPassword("It is Very very long string, don't now, what can I write here, just some letters");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.NEWPASSWORD);
    }

}