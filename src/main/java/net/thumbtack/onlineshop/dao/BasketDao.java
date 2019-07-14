package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Basket;
import net.thumbtack.onlineshop.model.BasketProductItem;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Product;

public interface BasketDao {
    Basket insertProduct(Basket basket, Product product, Integer count) throws OnlineShopException;

    Basket getByClient(Client client) throws OnlineShopException;

    void deleteProductFromBasket(Client client, Product product) throws OnlineShopException;

    void changeProductCount(Client client, BasketProductItem item) throws OnlineShopException;
}