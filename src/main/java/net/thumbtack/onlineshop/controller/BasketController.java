package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.request.BasketItemRequest;
import net.thumbtack.onlineshop.dto.response.BasketItemResponse;
import net.thumbtack.onlineshop.dto.response.EmptyResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/baskets")
public class BasketController {
    private final BasketService basketService;

    @Autowired
    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<BasketItemResponse> addToBasket(@CookieValue("JAVASESSIONID") String sessionId,
                                                @Valid @RequestBody BasketItemRequest request) throws OnlineShopException {
        return basketService.addProduct(sessionId, request);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BasketItemResponse> getBasket(@CookieValue("JAVASESSIONID") String sessionId) throws OnlineShopException {
        return basketService.getBasket(sessionId);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<BasketItemResponse> updateProductCount(@CookieValue("JAVASESSIONID") String sessionId,
                                                       @Valid @RequestBody BasketItemRequest request) throws OnlineShopException {
        return basketService.changeProductCount(sessionId, request);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse deleteProductFromBasket(@CookieValue("JAVASESSIONID") String sessionId,
                                                 @PathVariable("id") int productId) throws OnlineShopException {
        return basketService.deleteProductFromBasket(sessionId, productId);
    }
}