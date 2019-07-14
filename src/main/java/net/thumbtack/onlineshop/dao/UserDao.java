package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.User;

public interface UserDao {
    User getByLogin(String login) throws OnlineShopException;
}