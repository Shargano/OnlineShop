package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Administrator;

public interface AdminDao {
    Administrator insert(Administrator administrator) throws OnlineShopException;

    Administrator getById(int id) throws OnlineShopException;

    void update(Administrator administrator) throws OnlineShopException;

    void delete(Administrator administrator) throws OnlineShopException;
}