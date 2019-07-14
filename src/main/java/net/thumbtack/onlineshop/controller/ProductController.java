package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.request.ProductRequest;
import net.thumbtack.onlineshop.dto.request.UpdateProductRequest;
import net.thumbtack.onlineshop.dto.response.EmptyResponse;
import net.thumbtack.onlineshop.dto.response.ProductResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProductResponse create(@CookieValue("JAVASESSIONID") String sessionId,
                                  @Valid @RequestBody ProductRequest request) throws OnlineShopException {
        return productService.create(sessionId, request);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductResponse> getAll(@CookieValue("JAVASESSIONID") String sessionId,
                                        @RequestParam(name = "category", required = false) List<Integer> categories,
                                        @RequestParam(name = "order", required = false, defaultValue = "product") String order) throws OnlineShopException {
        return productService.getAll(sessionId, categories, order);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductResponse get(@CookieValue("JAVASESSIONID") String sessionId,
                               @PathVariable(value = "id") int productId) throws OnlineShopException {
        return productService.get(sessionId, productId);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProductResponse update(@CookieValue("JAVASESSIONID") String sessionId,
                                  @PathVariable(value = "id") int productId,
                                  @Valid @RequestBody UpdateProductRequest request) throws OnlineShopException {
        return productService.update(sessionId, productId, request);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse delete(@CookieValue("JAVASESSIONID") String sessionId,
                                @PathVariable(value = "id") int productId) throws OnlineShopException {
        return productService.delete(sessionId, productId);
    }
}