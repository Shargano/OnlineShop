package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.response.account.ClientAccountInfoResponse;
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
public class CreateClientAcceptanceTest extends TestBase {

    private void testInvalidRequest(CreateClientRequest request, ValidationErrorCode code) {
        try {
            createClient(request);
            fail();
        } catch (HttpClientErrorException exc) {
            assertNull(getCookie(exc));
            handleHttpClientException(exc, code);
        }
    }

    @Test
    public void registerClient() {
        CreateClientRequest request = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "88005553535", "SayHello", "123321qq");
        ClientAccountInfoResponse response = createClient(request).getBody();

        assertNotNull(response.getId());
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getAddress(), response.getAddress());
        assertEquals(request.getPhone(), response.getPhone());
        assertEquals(0, response.getDeposit().getValue());
    }

    @Test
    public void createAlreadyExistingClient() {
        CreateClientRequest request = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "SomeAddress", "8-800-555-35-35", "SayHello", "123321qq");
        createClient(request);
        try {
            createClient(request);
            fail();
        } catch (HttpClientErrorException exc) {
            assertNull(getCookie(exc));
            handleHttpClientException(exc, OnlineShopErrorCode.USER_ALREADY_EXISTS);
        }
    }

    @Test()
    public void createClientWithInvalidFirstName() {
        CreateClientRequest request = new CreateClientRequest("Misha", "Дедун", null, "mail@mail.com", "SomeAddress", "8-800-555-35-35", "SayHello", "123321qq");
        testInvalidRequest(request, ValidationErrorCode.FIRSTNAME);
        request.setFirstName("");
        testInvalidRequest(request, ValidationErrorCode.FIRSTNAME);
        request.setFirstName("       ");
        testInvalidRequest(request, ValidationErrorCode.FIRSTNAME);
        request.setFirstName(null);
        testInvalidRequest(request, ValidationErrorCode.FIRSTNAME);
        request.setFirstName("It is Very very long string, don't now, what can I write here, just some letters");
        testInvalidRequest(request, ValidationErrorCode.FIRSTNAME);
    }

    @Test()
    public void createClientWithInvalidLastName() {
        CreateClientRequest request = new CreateClientRequest("Михаил", "Dedun", null, "mail@mail.com", "SomeAddress", "8-800-555-35-35", "SayHello", "123321qq");
        testInvalidRequest(request, ValidationErrorCode.LASTNAME);
        request.setLastName("");
        testInvalidRequest(request, ValidationErrorCode.LASTNAME);
        request.setLastName("       ");
        testInvalidRequest(request, ValidationErrorCode.LASTNAME);
        request.setLastName(null);
        testInvalidRequest(request, ValidationErrorCode.LASTNAME);
        request.setLastName("It is Very very long string, don't now, what can I write here, just some letters");
        testInvalidRequest(request, ValidationErrorCode.LASTNAME);
    }

    @Test()
    public void createClientWithInvalidEmail() {
        CreateClientRequest request = new CreateClientRequest("Михаил", "Дедун", null, "notMail", "SomeAddress", "8-800-555-35-35", "SayHello", "123321qq");
        testInvalidRequest(request, ValidationErrorCode.EMAIL);
        request.setEmail("");
        testInvalidRequest(request, ValidationErrorCode.EMAIL);
        request.setEmail("       ");
        testInvalidRequest(request, ValidationErrorCode.EMAIL);
        request.setEmail(null);
        testInvalidRequest(request, ValidationErrorCode.EMAIL);
        request.setEmail("It is Very very long string, don't now, what can I write here, just some letters");
        testInvalidRequest(request, ValidationErrorCode.EMAIL);
    }

    @Test()
    public void createClientWithInvalidAddress() {
        CreateClientRequest request = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "", "8-800-555-35-35", "SayHello", "123321qq");
        testInvalidRequest(request, ValidationErrorCode.ADDRESS);
        request.setAddress("       ");
        testInvalidRequest(request, ValidationErrorCode.ADDRESS);
        request.setAddress(null);
        testInvalidRequest(request, ValidationErrorCode.ADDRESS);
        request.setAddress("It is Very very long string, don't now, what can I write here, just some letters");
        testInvalidRequest(request, ValidationErrorCode.ADDRESS);
    }

    @Test()
    public void createClientWithInvalidPhone() {
        CreateClientRequest request = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "someAddress", "8-00-555-35-35", "SayHello", "123321qq");
        testInvalidRequest(request, ValidationErrorCode.PHONE);
        request.setPhone("notPhone");
        testInvalidRequest(request, ValidationErrorCode.PHONE);
        request.setPhone("930-230");
        testInvalidRequest(request, ValidationErrorCode.PHONE);
        request.setPhone("       ");
        testInvalidRequest(request, ValidationErrorCode.PHONE);
        request.setPhone(null);
        testInvalidRequest(request, ValidationErrorCode.PHONE);
        request.setPhone("It is Very very long string, don't now, what can I write here, just some letters");
        testInvalidRequest(request, ValidationErrorCode.PHONE);
    }

    @Test()
    public void createClientWithInvalidLogin() {
        CreateClientRequest request = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "someAddress", "8-800-555-35-35", "*#$%: ", "123321qq");
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
    public void createClientWithInvalidPassword() {
        CreateClientRequest request = new CreateClientRequest("Михаил", "Дедун", null, "mail@mail.com", "someAddress", "8-800-555-35-35", "SayHello", "");
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