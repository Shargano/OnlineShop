package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Product;

import java.util.List;

public interface ProductDao {
    Product insert(Product product) throws OnlineShopException;

    Product getById(int id, boolean throwExceptionIfNotFound) throws OnlineShopException;

    List<Product> getAllOrderProduct(List<Integer> categories) throws OnlineShopException;

    List<Product> getAllOrderCategory(List<Integer> categories) throws OnlineShopException;

    void update(Product product) throws OnlineShopException;

    void delete(Product product) throws OnlineShopException;
}