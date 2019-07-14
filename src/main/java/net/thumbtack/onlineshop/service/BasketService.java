package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dao.*;
import net.thumbtack.onlineshop.dto.request.BasketItemRequest;
import net.thumbtack.onlineshop.dto.response.BasketItemResponse;
import net.thumbtack.onlineshop.dto.response.EmptyResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Basket;
import net.thumbtack.onlineshop.model.BasketProductItem;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BasketService extends ServiceBase {
    private final BasketDao basketDao;
    private final ProductDao productDao;

    public BasketService(SessionDao sessionDao, AdminDao adminDao, ClientDao clientDao, BasketDao basketDao, ProductDao productDAO) {
        super(sessionDao, adminDao, clientDao);
        this.basketDao = basketDao;
        this.productDao = productDAO;
    }

    public List<BasketItemResponse> addProduct(String sessionId, BasketItemRequest request) throws OnlineShopException {
        Client client = getClientFromDB(sessionId);
        Product product = productDao.getById(request.getId(), true);
        checkProductFields(product, request);
        basketDao.insertProduct(client.getBasket(), product, request.getCount());
        return makeBasketResponse(client.getBasket());
    }

    public List<BasketItemResponse> getBasket(String sessionId) throws OnlineShopException {
        Client client = getClientFromDB(sessionId);
        return makeBasketResponse(client.getBasket());
    }

    public EmptyResponse deleteProductFromBasket(String sessionId, int productId) throws OnlineShopException {
        Client client = getClientFromDB(sessionId);
        Product product = client.getProductFromBasket(productId);
        if (product == null)
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_IN_BASKET);
        basketDao.deleteProductFromBasket(client, product);
        return new EmptyResponse();
    }

    public List<BasketItemResponse> changeProductCount(String sessionId, BasketItemRequest request) throws OnlineShopException {
        Client client = getClientFromDB(sessionId);
        BasketProductItem item = client.getItemFromBasket(request.getId());
        if (item == null)
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_IN_BASKET);
        item.setCount(request.getCount());
        basketDao.changeProductCount(client, item);
        return makeBasketResponse(client.getBasket());
    }

    private void checkProductFields(Product product, BasketItemRequest request) throws OnlineShopException {
        if (product.getPrice() != request.getPrice())
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_PRICE_INCORRECT);
        if (!product.getName().equals(request.getName()))
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NAME_INCORRECT);
    }

    private List<BasketItemResponse> makeBasketResponse(Basket basket) {
        List<BasketItemResponse> responses = new ArrayList<>();
        basket.getItems().forEach(item -> responses.add(new BasketItemResponse(item.getProduct().getId(), item.getProduct().getName(), item.getProduct().getPrice(), item.getCount())));
        return responses;
    }
}