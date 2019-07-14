package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.response.SettingResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GetSettingsAcceptanceTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private final String url = "http://localhost:" + port + "/api/settings";

    @Test
    public void testAdminGetSettings() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        String registerCookie = getCookie(createAdmin(createAdminRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, registerCookie);
        SettingResponse settingResponse = template.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), SettingResponse.class).getBody();

        assertEquals(8888, settingResponse.getRest_http_port());
        assertEquals(50, settingResponse.getMaxNameLength());
        assertEquals(8, settingResponse.getMinPasswordLength());
    }
}