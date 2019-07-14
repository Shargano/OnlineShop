package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Session;

public interface SessionDao {
    Session insert(Session session) throws OnlineShopException;

    Session getById(String id) throws OnlineShopException;

    Session getByUserId(int id) throws OnlineShopException;

    void delete(Session session) throws OnlineShopException;
}