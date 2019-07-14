package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.response.account.AdminAccountInfoResponse;
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
public class CreateAdminAcceptanceTest extends TestBase {

    private void testInvalidRequest(CreateAdminRequest request, ValidationErrorCode code) {
        try {
            createAdmin(request);
            fail();
        } catch (HttpClientErrorException exc) {
            assertNull(getCookie(exc));
            handleHttpClientException(exc, code);
        }
    }

    @Test
    public void createAdmin() {
        CreateAdminRequest request = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        AdminAccountInfoResponse response = createAdmin(request).getBody();

        assertNotNull(response.getId());
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getPosition(), response.getPosition());
    }

    @Test
    public void createAlreadyExistingAdmin() {
        CreateAdminRequest request = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        createAdmin(request);
        try {
            createAdmin(request);
            fail();
        } catch (HttpClientErrorException exc) {
            assertNull(getCookie(exc));
            handleHttpClientException(exc, OnlineShopErrorCode.USER_ALREADY_EXISTS);
        }
    }

    @Test()
    public void createAdminWithInvalidFirstName() {
        CreateAdminRequest request = new CreateAdminRequest("Misha", "Дедун", null, "Leader", "SayHello", "123321qq");
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
    public void createAdminWithInvalidLastName() {
        CreateAdminRequest request = new CreateAdminRequest("Михаил", "Dedun", null, "Leader", "SayHello", "123321qq");
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
    public void createAdminWithInvalidPosition() {
        CreateAdminRequest request = new CreateAdminRequest("Михаил", "Дедун", null, "", "SayHello", "123321qq");
        testInvalidRequest(request, ValidationErrorCode.POSITION);
        request.setPosition("      ");
        testInvalidRequest(request, ValidationErrorCode.POSITION);
        request.setPosition(null);
        testInvalidRequest(request, ValidationErrorCode.POSITION);
    }

    @Test()
    public void createAdminWithInvalidLogin() {
        CreateAdminRequest request = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "&*()! ", "123321qq");
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
    public void createAdminWithInvalidPassword() {
        CreateAdminRequest request = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "");
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