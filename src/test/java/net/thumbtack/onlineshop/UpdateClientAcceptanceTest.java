package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.request.UpdateClientRequest;
import net.thumbtack.onlineshop.dto.response.account.AdminAccountInfoResponse;
import net.thumbtack.onlineshop.dto.response.account.ClientAccountInfoResponse;
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
public class UpdateClientAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/clients";

    private void testInvalidRequest(UpdateClientRequest request, String cookie, ValidationErrorCode code) {
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
    public void testUpdateClient() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "8-800-555-35-35", "SayHello", "123321qq");
        String registerCookie = getCookie(createClient(createClientRequest));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);
        UpdateClientRequest updateClientRequest = new UpdateClientRequest("Мишко", "Дедун", null, "mail@mail.com", "New Address", "89991112244", "123321qq", "123321Google");

        template.exchange(url, HttpMethod.PUT, new HttpEntity<>(updateClientRequest, headers), ClientAccountInfoResponse.class);
        ClientAccountInfoResponse getResponse = template.exchange("http://localhost:" + port + "/api/accounts", HttpMethod.GET, new HttpEntity<>(null, headers), ClientAccountInfoResponse.class).getBody();

        assertEquals(updateClientRequest.getFirstName(), getResponse.getFirstName());
        assertEquals(updateClientRequest.getLastName(), getResponse.getLastName());
        assertEquals(updateClientRequest.getPatronymic(), getResponse.getPatronymic());
        assertEquals(updateClientRequest.getAddress(), getResponse.getAddress());
        assertEquals(updateClientRequest.getEmail(), getResponse.getEmail());
        assertEquals(updateClientRequest.getPhone(), getResponse.getPhone());
    }

    @Test
    public void testAdminTryToUpdateClient() {
        CreateAdminRequest request = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(request));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);
        UpdateClientRequest updateClientRequest = new UpdateClientRequest("Мишко", "Дедун", null, "mail@mail.com", "New Address", "89991112244", "123321qq", "123321Google");
        try {
            template.exchange(url, HttpMethod.PUT, new HttpEntity<>(updateClientRequest, headers), ClientAccountInfoResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.USER_NOT_CLIENT);
        }
    }

    @Test
    public void testUpdateClientIncorrectPassword() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "8-800-555-35-35", "SayHello", "123321qq");
        String registerCookie = getCookie(createClient(createClientRequest));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);
        UpdateClientRequest updateClientRequest = new UpdateClientRequest("Мишко", "Дедун", null, "mail@mail.com", "New Address", "89991112244", "notMyPassword", "123321Google");
        try {
            template.exchange(url, HttpMethod.PUT, new HttpEntity<>(updateClientRequest, headers), ClientAccountInfoResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.PASSWORD_FAILED);
        }
    }

    @Test
    public void testUpdateClientNotLogged() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "8-800-555-35-35", "SayHello", "123321qq");
        String registerCookie = getCookie(createClient(createClientRequest));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);
        UpdateClientRequest updateClientRequest = new UpdateClientRequest("Мишко", "Дедун", null, "mail@mail.com", "New Address", "89991112244", "123321qq", "123321Google");

        logoutUser(registerCookie);
        try {
            template.exchange(url, HttpMethod.PUT, new HttpEntity<>(updateClientRequest, headers), ClientAccountInfoResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.SESSION_NOT_EXIST);
        }
    }

    @Test()
    public void testUpdateClientWithInvalidFirstName() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "8-800-555-35-35", "SayHello", "123321qq");
        String registerCookie = getCookie(createClient(createClientRequest));
        UpdateClientRequest request = new UpdateClientRequest("Michael", "Дедун", null, "mail@mail.com", "New Address", "89991112244", "123321qq", "123321Google");
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
    public void testUpdateClientWithInvalidLastName() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "8-800-555-35-35", "SayHello", "123321qq");
        String registerCookie = getCookie(createClient(createClientRequest));
        UpdateClientRequest request = new UpdateClientRequest("Михаил", "Dedun", null, "mail@mail.com", "New Address", "89991112244", "123321qq", "123321Google");
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
    public void testUpdateClientWithInvalidEmail() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "8-800-555-35-35", "SayHello", "123321qq");
        String registerCookie = getCookie(createClient(createClientRequest));
        UpdateClientRequest request = new UpdateClientRequest("Михаил", "Дедун", null, "some str", "New Address", "89991112244", "123321qq", "123321Google");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.EMAIL);
        request.setEmail("");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.EMAIL);
        request.setEmail("       ");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.EMAIL);
        request.setEmail(null);
        testInvalidRequest(request, registerCookie, ValidationErrorCode.EMAIL);
        request.setEmail("It is Very very long string, don't now, what can I write here, just some letters");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.EMAIL);
    }

    @Test()
    public void testUpdateClientWithInvalidAddress() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "8-800-555-35-35", "SayHello", "123321qq");
        String registerCookie = getCookie(createClient(createClientRequest));
        UpdateClientRequest request = new UpdateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "", "89991112244", "123321qq", "123321Google");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.ADDRESS);
        request.setAddress("       ");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.ADDRESS);
        request.setAddress(null);
        testInvalidRequest(request, registerCookie, ValidationErrorCode.ADDRESS);
    }

    @Test()
    public void testUpdateClientWithInvalidPhone() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "8-800-555-35-35", "SayHello", "123321qq");
        String registerCookie = getCookie(createClient(createClientRequest));
        UpdateClientRequest request = new UpdateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "new Address", "8-00-55-5-35", "123321qq", "123321Google");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.PHONE);
        request.setPhone("notPhone");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.PHONE);
        request.setPhone("930-230");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.PHONE);
        request.setPhone("       ");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.PHONE);
        request.setPhone(null);
        testInvalidRequest(request, registerCookie, ValidationErrorCode.PHONE);
        request.setPhone("It is Very very long string, don't now, what can I write here, just some letters");
        testInvalidRequest(request, registerCookie, ValidationErrorCode.PHONE);
    }

    @Test()
    public void testUpdateClientWithInvalidOldPassword() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "8-800-555-35-35", "SayHello", "123321qq");
        String registerCookie = getCookie(createClient(createClientRequest));
        UpdateClientRequest request = new UpdateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "New Address", "89991112244", "", "123321Google");
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
    public void testUpdateClientWithInvalidNewPassword() {
        CreateClientRequest createClientRequest = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "8-800-555-35-35", "SayHello", "123321qq");
        String registerCookie = getCookie(createClient(createClientRequest));
        UpdateClientRequest request = new UpdateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "New Address", "89991112244", "123321qq", "");
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