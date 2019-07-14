package net.thumbtack.onlineshop.dao;


import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Category;

import java.util.List;

public interface CategoryDao {
    Category insert(Category category) throws OnlineShopException;

    Category getById(int id) throws OnlineShopException;

    List<Category> getByListOfId(List<Integer> idList) throws OnlineShopException;

    List<Category> getAll() throws OnlineShopException;

    void update(Category category) throws OnlineShopException;

    void delete(Category category) throws OnlineShopException;

    void deleteAll() throws OnlineShopException;
}