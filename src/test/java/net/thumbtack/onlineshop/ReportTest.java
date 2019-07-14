package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.dto.request.*;
import net.thumbtack.onlineshop.dto.response.*;
import net.thumbtack.onlineshop.dto.response.account.AdminAccountInfoResponse;
import net.thumbtack.onlineshop.dto.response.account.ClientAccountInfoResponse;
import net.thumbtack.onlineshop.dto.response.report.*;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ReportTest extends TestBase {
    private RestTemplate template = new RestTemplate();
    private String productsUrl = "http://localhost:" + port + "/api/products";
    private String purchasesUrl = "http://localhost:" + port + "/api/purchases";
    private String depositsUrl = "http://localhost:" + port + "/api/deposits";
    private String accountsUrl = "http://localhost:" + port + "/api/accounts";
    private String basketsUrl = "http://localhost:" + port + "/api/baskets";

    private String adminCookie;
    private HttpHeaders adminHeaders = new HttpHeaders();
    private HttpHeaders firstClientHeaders = new HttpHeaders();
    private HttpHeaders secondClientHeaders = new HttpHeaders();
    private HttpHeaders thirdClientHeaders = new HttpHeaders();

    @Test
    public void testGetReport() {
        registerAdmin();
        ProductResponse book = createProduct(new ProductRequest("books", 223, 2000, null), adminCookie).getBody();
        ProductResponse apple = createProduct(new ProductRequest("apple", 101, 1000, null), adminCookie).getBody();
        CategoryResponse vevoCategory = createCategory(new CategoryRequest("Vevo", null), adminCookie).getBody();
        CategoryResponse carsCategory = createCategory(new CategoryRequest("Cars", null), adminCookie).getBody();
        List<Integer> categories1 = new ArrayList<>();
        categories1.add(vevoCategory.getId());
        ProductResponse cheese = createProduct(new ProductRequest("cheese", 32, 6100, categories1), adminCookie).getBody();
        ProductResponse zero = createProduct(new ProductRequest("zero", 54, 3500, categories1), adminCookie).getBody();
        List<Integer> categories2 = new ArrayList<>();
        categories2.add(carsCategory.getId());
        ProductResponse audi = createProduct(new ProductRequest("audi", 87, 1000, categories2), adminCookie).getBody();

        CreateClientRequest firstClientCreateRequest = new CreateClientRequest("Даниил", "Мусник", null, "dantesss@mail.com", "SomeAddress", "88005553535", "Dantes55", "123321qq");
        ClientAccountInfoResponse firstClientCreateResponse = createClient(firstClientCreateRequest, firstClientHeaders);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(productsUrl)
                .queryParam("order", "product")
                .queryParam("category", vevoCategory.getId(), carsCategory.getId());
        ResponseEntity<List<ProductResponse>> responseEntity = template.exchange(builder.build().toUri(), HttpMethod.GET, new HttpEntity<>(null, firstClientHeaders), new ParameterizedTypeReference<List<ProductResponse>>() {
        });
        List<ProductResponse> getAllProductsResponse = responseEntity.getBody();
        assertEquals(3, getAllProductsResponse.size());
        assertEquals(audi, getAllProductsResponse.get(0));
        assertEquals(cheese, getAllProductsResponse.get(1));
        assertEquals(zero, getAllProductsResponse.get(2));

        PurchaseProductRequest purchaseBookRequest = new PurchaseProductRequest(book.getId(), book.getName(), book.getPrice(), 10);
        PurchaseResponse firstClientPurchaseBookResponse = template.exchange(purchasesUrl, HttpMethod.POST, new HttpEntity<>(purchaseBookRequest, firstClientHeaders), PurchaseResponse.class).getBody();
        assertEquals(purchaseBookRequest.getId(), firstClientPurchaseBookResponse.getId());
        assertEquals(purchaseBookRequest.getName(), firstClientPurchaseBookResponse.getName());
        assertEquals(purchaseBookRequest.getPrice(), firstClientPurchaseBookResponse.getPrice());
        assertEquals(purchaseBookRequest.getCount(), firstClientPurchaseBookResponse.getCount());
        PurchaseProductRequest purchaseCheeseRequest = new PurchaseProductRequest(cheese.getId(), cheese.getName(), cheese.getPrice(), 29);
        template.exchange(purchasesUrl, HttpMethod.POST, new HttpEntity<>(purchaseCheeseRequest, firstClientHeaders), PurchaseResponse.class);
        BasketItemRequest firstClientAddCheeseToBasketRequest = new BasketItemRequest(cheese.getId(), cheese.getName(), cheese.getPrice(), null);
        List<BasketItemResponse> firstClientBasketResponse = template.exchange(basketsUrl, HttpMethod.POST, new HttpEntity<>(firstClientAddCheeseToBasketRequest, firstClientHeaders), new ParameterizedTypeReference<List<BasketItemResponse>>() {
        }).getBody();
        assertEquals(1, firstClientBasketResponse.size());
        assertEquals(firstClientAddCheeseToBasketRequest.getId(), firstClientBasketResponse.get(0).getId());
        assertEquals(firstClientAddCheeseToBasketRequest.getName(), firstClientBasketResponse.get(0).getName());
        assertEquals(firstClientAddCheeseToBasketRequest.getPrice(), firstClientBasketResponse.get(0).getPrice());
        assertEquals(1, (int) firstClientBasketResponse.get(0).getCount());
        PurchaseProductRequest purchaseAppleRequest = new PurchaseProductRequest(apple.getId(), apple.getName(), apple.getPrice(), 49);
        template.exchange(purchasesUrl, HttpMethod.POST, new HttpEntity<>(purchaseAppleRequest, firstClientHeaders), PurchaseResponse.class);

        CreateClientRequest secondClientCreateRequest = new CreateClientRequest("Максим", "Собин", "Алексеевич", "sobin32@gmail.com", "Omsk", "88005553535", "UnderTheGun", "123321qq");
        ClientAccountInfoResponse secondClientCreateResponse = createClient(secondClientCreateRequest, secondClientHeaders);
        try {
            UpdateProductRequest updateAudiByClientRequest = new UpdateProductRequest("NewUnusualName", 10, 99, null);
            template.exchange(productsUrl + "/" + audi.getId(), HttpMethod.PUT, new HttpEntity<>(updateAudiByClientRequest, secondClientHeaders), ProductResponse.class);
            fail();
        } catch (HttpClientErrorException exc) {
            handleHttpClientException(exc, OnlineShopErrorCode.USER_NOT_ADMIN);
        }
        BasketItemRequest secondClientAddZeroToBasketRequest = new BasketItemRequest(zero.getId(), zero.getName(), zero.getPrice(), 3);
        BasketItemRequest secondClientAddAudiToBasketRequest = new BasketItemRequest(audi.getId(), audi.getName(), audi.getPrice(), 21);
        template.exchange(basketsUrl, HttpMethod.POST, new HttpEntity<>(secondClientAddZeroToBasketRequest, secondClientHeaders), new ParameterizedTypeReference<List<BasketItemResponse>>() {
        });
        List<BasketItemResponse> secondClientBasketResponse = template.exchange(basketsUrl, HttpMethod.POST, new HttpEntity<>(secondClientAddAudiToBasketRequest, secondClientHeaders), new ParameterizedTypeReference<List<BasketItemResponse>>() {
        }).getBody();
        List<PurchaseProductRequest> secondClientBuyBasketRequests = new ArrayList<>();
        secondClientBuyBasketRequests.add(new PurchaseProductRequest(secondClientBasketResponse.get(0).getId(), secondClientBasketResponse.get(0).getName(), secondClientBasketResponse.get(0).getPrice(), 3));
        secondClientBuyBasketRequests.add(new PurchaseProductRequest(secondClientBasketResponse.get(1).getId(), secondClientBasketResponse.get(1).getName(), secondClientBasketResponse.get(1).getPrice(), 20));
        BuyBasketResponse secondClientBuyBasketResponse = template.exchange(purchasesUrl + "/baskets", HttpMethod.POST, new HttpEntity<>(secondClientBuyBasketRequests, secondClientHeaders), BuyBasketResponse.class).getBody();
        assertEquals(2, secondClientBuyBasketResponse.getBought().size());
        assertEquals(3, (int) secondClientBuyBasketResponse.getBought().get(0).getCount());
        assertEquals(zero.getId(), secondClientBuyBasketResponse.getBought().get(0).getId());
        assertEquals(20, (int) secondClientBuyBasketResponse.getBought().get(1).getCount());
        assertEquals(audi.getId(), secondClientBuyBasketResponse.getBought().get(1).getId());
        assertEquals(1, secondClientBuyBasketResponse.getRemaining().size());

        template.exchange(productsUrl + "/" + cheese.getId(), HttpMethod.DELETE, new HttpEntity<>(null, adminHeaders), EmptyResponse.class);
        UpdateProductRequest updateAppleRequest = new UpdateProductRequest("newNameForApple", null, null, null);
        apple = template.exchange(productsUrl + "/" + apple.getId(), HttpMethod.PUT, new HttpEntity<>(updateAppleRequest, adminHeaders), ProductResponse.class).getBody();

        CreateClientRequest thirdClientCreateRequest = new CreateClientRequest("Андрей", "Шмидер", "Сергеевич", "Germany@gmail.com", "Omsk", "88005553535", "BeenToHell", "123321qq");
        ClientAccountInfoResponse thirdClientCreateResponse = createClient(thirdClientCreateRequest, thirdClientHeaders);
        purchaseAppleRequest = new PurchaseProductRequest(apple.getId(), apple.getName(), apple.getPrice(), 14);
        PurchaseProductRequest purchaseZeroRequest = new PurchaseProductRequest(zero.getId(), zero.getName(), zero.getPrice(), 61);
        PurchaseProductRequest purchaseAudiRequest = new PurchaseProductRequest(audi.getId(), audi.getName(), audi.getPrice(), 3);
        template.exchange(purchasesUrl, HttpMethod.POST, new HttpEntity<>(purchaseAppleRequest, thirdClientHeaders), PurchaseResponse.class);
        template.exchange(purchasesUrl, HttpMethod.POST, new HttpEntity<>(purchaseZeroRequest, thirdClientHeaders), PurchaseResponse.class);
        template.exchange(purchasesUrl, HttpMethod.POST, new HttpEntity<>(purchaseAudiRequest, thirdClientHeaders), PurchaseResponse.class);
        purchaseAudiRequest.setCount(33);
        template.exchange(purchasesUrl, HttpMethod.POST, new HttpEntity<>(purchaseAudiRequest, thirdClientHeaders), PurchaseResponse.class);

        BuyBasketResponse firstClientBuyBasketResponse = template.exchange(purchasesUrl + "/baskets", HttpMethod.POST, new HttpEntity<>(firstClientBasketResponse, firstClientHeaders), BuyBasketResponse.class).getBody();
        assertEquals(1, firstClientBuyBasketResponse.getRemaining().size());

        builder = UriComponentsBuilder.fromUriString(purchasesUrl)
                .queryParam("client", "");
        ClientReportResponse clientReportResponse = template.exchange(builder.build().toUri(), HttpMethod.GET, new HttpEntity<>(null, adminHeaders), ClientReportResponse.class).getBody();
        assertEquals(3, clientReportResponse.getReport().size());
        builder = UriComponentsBuilder.fromUriString(purchasesUrl)
                .queryParam("client", thirdClientCreateResponse.getId());
        clientReportResponse = template.exchange(builder.build().toUri(), HttpMethod.GET, new HttpEntity<>(null, adminHeaders), ClientReportResponse.class).getBody();
        List<ClientReport> thirdClientReportResponse = clientReportResponse.getReport();
        assertEquals(1, thirdClientReportResponse.size());
        assertEquals(7840, thirdClientReportResponse.get(0).getCostOfPurchases());
        assertEquals(thirdClientCreateResponse.getId(), thirdClientReportResponse.get(0).getId());
        assertEquals(thirdClientCreateResponse.getFirstName(), thirdClientReportResponse.get(0).getFirstName());
        assertEquals(thirdClientCreateResponse.getLastName(), thirdClientReportResponse.get(0).getLastName());
        assertEquals(thirdClientCreateResponse.getEmail(), thirdClientReportResponse.get(0).getEmail());
        assertEquals(thirdClientCreateResponse.getAddress(), thirdClientReportResponse.get(0).getAddress());
        assertEquals(thirdClientCreateResponse.getPhone(), thirdClientReportResponse.get(0).getPhone());
        List<PurchaseResponse> thirdClientPurchases = thirdClientReportResponse.get(0).getPurchases();
        assertEquals(4, thirdClientPurchases.size());
        assertEquals(apple.getName(), thirdClientPurchases.get(0).getName());
        assertEquals(apple.getPrice(), thirdClientPurchases.get(0).getPrice());
        assertEquals(zero.getName(), thirdClientPurchases.get(1).getName());
        assertEquals(zero.getPrice(), thirdClientPurchases.get(1).getPrice());
        assertEquals(audi.getName(), thirdClientPurchases.get(2).getName());
        assertEquals(audi.getPrice(), thirdClientPurchases.get(2).getPrice());
        assertEquals(audi.getName(), thirdClientPurchases.get(3).getName());
        assertEquals(audi.getPrice(), thirdClientPurchases.get(3).getPrice());
        assertNotEquals(thirdClientPurchases.get(2).getCount(), thirdClientPurchases.get(3).getCount());

        builder = UriComponentsBuilder.fromUriString(purchasesUrl)
                .queryParam("client", "")
                .queryParam("onlyTotal", "true");
        clientReportResponse = template.exchange(builder.build().toUri(), HttpMethod.GET, new HttpEntity<>(null, adminHeaders), ClientReportResponse.class).getBody();
        assertEquals(0, clientReportResponse.getReport().size());
        assertEquals(17849, (int) clientReportResponse.getTotalCostOfSales());

        builder = UriComponentsBuilder.fromUriString(purchasesUrl)
                .queryParam("product", "")
                .queryParam("offset", "2")
                .queryParam("limit", "5");
        ProductReportResponse productReportResponse = template.exchange(builder.build().toUri(), HttpMethod.GET, new HttpEntity<>(null, adminHeaders), ProductReportResponse.class).getBody();
        List<ProductReport> productReports = productReportResponse.getReport();
        assertEquals(5, productReports.size());
        checkProductItem(productReports.get(0), audi.getId(), audi.getName(), audi.getPrice());
        checkProductItem(productReports.get(1), audi.getId(), audi.getName(), audi.getPrice());
        checkProductItem(productReports.get(2), book.getId(), book.getName(), book.getPrice());
        checkProductItem(productReports.get(3), null, cheese.getName(), cheese.getPrice());
        checkProductItem(productReports.get(4), apple.getId(), apple.getName(), apple.getPrice());

        builder = UriComponentsBuilder.fromUriString(purchasesUrl)
                .queryParam("product", "")
                .queryParam("onlyTotal", "true");
        productReportResponse = template.exchange(builder.build().toUri(), HttpMethod.GET, new HttpEntity<>(null, adminHeaders), ProductReportResponse.class).getBody();
        assertEquals(0, productReportResponse.getReport().size());
        assertEquals(222, (int) productReportResponse.getTotalCountOfSales());

        builder = UriComponentsBuilder.fromUriString(purchasesUrl)
                .queryParam("category", "")
                .queryParam("limit", "9");
        CategoryReportResponse categoryReportResponse = template.exchange(builder.build().toUri(), HttpMethod.GET, new HttpEntity<>(null, adminHeaders), CategoryReportResponse.class).getBody();
        List<CategoryReport> categoryReports = categoryReportResponse.getReport();
        assertEquals(9, categoryReports.size());
        checkCategoryItem(categoryReports.get(0), null, null, null, firstClientCreateResponse.getId(), "apple");
        checkCategoryItem(categoryReports.get(1), null, null, null, firstClientCreateResponse.getId(), book.getName());
        checkCategoryItem(categoryReports.get(2), null, null, null, firstClientCreateResponse.getId(), cheese.getName());
        checkCategoryItem(categoryReports.get(3), null, null, null, thirdClientCreateResponse.getId(), apple.getName());
        checkCategoryItem(categoryReports.get(4), carsCategory.getId(), carsCategory.getName(), null, secondClientCreateResponse.getId(), audi.getName());
        checkCategoryItem(categoryReports.get(5), carsCategory.getId(), carsCategory.getName(), null, thirdClientCreateResponse.getId(), audi.getName());
        checkCategoryItem(categoryReports.get(6), carsCategory.getId(), carsCategory.getName(), null, thirdClientCreateResponse.getId(), audi.getName());
        checkCategoryItem(categoryReports.get(7), vevoCategory.getId(), vevoCategory.getName(), null, secondClientCreateResponse.getId(), zero.getName());
        checkCategoryItem(categoryReports.get(8), vevoCategory.getId(), vevoCategory.getName(), null, thirdClientCreateResponse.getId(), zero.getName());

        builder = UriComponentsBuilder.fromUriString(purchasesUrl)
                .queryParam("category", "")
                .queryParam("onlyTotal", "true");
        categoryReportResponse = template.exchange(builder.build().toUri(), HttpMethod.GET, new HttpEntity<>(null, adminHeaders), CategoryReportResponse.class).getBody();
        assertEquals(0, categoryReportResponse.getReport().size());
        assertEquals(222, (int) categoryReportResponse.getTotalCountOfSales());
    }

    private void registerAdmin() {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest("Михаил", "Дедун", null, "Leader", "SayHello", "123321qq");
        adminCookie = getCookie(createAdmin(createAdminRequest));
        adminHeaders = new HttpHeaders();
        adminHeaders.add("Cookie", adminCookie);
        AdminAccountInfoResponse infoResponse = template.exchange(accountsUrl, HttpMethod.GET, new HttpEntity<>(null, adminHeaders), AdminAccountInfoResponse.class).getBody();
        assertNotNull(infoResponse.getId());
        assertEquals(createAdminRequest.getFirstName(), infoResponse.getFirstName());
        assertEquals(createAdminRequest.getLastName(), infoResponse.getLastName());
        assertEquals(createAdminRequest.getPatronymic(), infoResponse.getPatronymic());
        assertEquals(createAdminRequest.getPosition(), infoResponse.getPosition());
    }

    private ClientAccountInfoResponse createClient(CreateClientRequest request, HttpHeaders headers) {
        String cookie = getCookie(createClient(request));
        headers.add("Cookie", cookie);
        DepositRequest firstClientDepositRequest = new DepositRequest(10000);
        return template.exchange(depositsUrl, HttpMethod.PUT, new HttpEntity<>(firstClientDepositRequest, headers), ClientAccountInfoResponse.class).getBody();
    }

    private void checkProductItem(ProductReport productReport, Integer id, String name, int price) {
        assertEquals(id, productReport.getProductId());
        assertEquals(name, productReport.getProductName());
        assertEquals(price, productReport.getProductPrice());
    }

    private void checkCategoryItem(CategoryReport categoryReport, Integer id, String name, String parentName, int clientId, String productName) {
        assertEquals(id, categoryReport.getId());
        assertEquals(name, categoryReport.getName());
        assertEquals(parentName, categoryReport.getParentName());
        assertEquals(clientId, categoryReport.getClient().getId());
        assertEquals(productName, categoryReport.getPurchase().getName());
    }
}