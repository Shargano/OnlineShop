package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dao.ProductDao;
import net.thumbtack.onlineshop.dto.request.ProductRequest;
import net.thumbtack.onlineshop.dto.request.UpdateProductRequest;
import net.thumbtack.onlineshop.dto.response.ProductResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Administrator;
import net.thumbtack.onlineshop.model.Product;
import net.thumbtack.onlineshop.model.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProductServiceTest extends BaseServiceTest {
    private Administrator admin = new Administrator("Sasha", "Michael", "Leader", "SayHello", "12321qq");
    private String sessionId = UUID.randomUUID().toString();
    private Product product = new Product("name", 1243, 12, null);

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Before
    public void setUp() throws OnlineShopException {
        MockitoAnnotations.initMocks(this);
        when(adminDao.insert(any(Administrator.class))).thenReturn(admin);
        when(adminDao.getById(any(Integer.class))).thenReturn(admin);

        when(sessionDao.insert(any(Session.class))).thenReturn(new Session(sessionId, admin));
        when(sessionDao.getById(any(String.class))).thenReturn(new Session(sessionId, admin));

        when(productDao.insert(any(Product.class))).thenReturn(product);
        when(productDao.getById(any(Integer.class), any(Boolean.class))).thenReturn(product);
    }

    @Test
    public void testCreateProduct() throws OnlineShopException {
        ProductRequest request = new ProductRequest("name", 1243, 12, null);
        ProductResponse response = productService.create(sessionId, request);

        assertEquals(request.getName(), response.getName());
        assertEquals(request.getPrice(), response.getPrice());
        assertEquals((int) request.getCount(), response.getCount());
        assertEquals(new ArrayList<>(), response.getCategories());
    }

    @Test
    public void testGetProduct() throws OnlineShopException {
        ProductRequest request = new ProductRequest("name", 1243, 12, null);
        ProductResponse createResponse = productService.create(sessionId, request);

        ProductResponse getResponse = productService.get(sessionId, createResponse.getId());
        assertEquals(createResponse.getName(), getResponse.getName());
        assertEquals(createResponse.getPrice(), getResponse.getPrice());
        assertEquals(createResponse.getCount(), getResponse.getCount());
        assertEquals(createResponse.getCategories(), getResponse.getCategories());
    }

    @Test
    public void testUpdateProduct() throws OnlineShopException {
        ProductRequest createRequest = new ProductRequest("name", 1243, 12, null);
        ProductResponse createResponse = productService.create(sessionId, createRequest);
        UpdateProductRequest request = new UpdateProductRequest("nameNew", 100, 111, null);
        ProductResponse updateResponse = productService.update(sessionId, product.getId(), request);

        assertEquals(request.getName(), updateResponse.getName());
        assertEquals((int) request.getPrice(), updateResponse.getPrice());
        assertEquals((int) request.getCount(), updateResponse.getCount());
        assertEquals(createResponse.getCategories(), updateResponse.getCategories());
    }
}
