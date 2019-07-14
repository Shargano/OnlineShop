package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Client;

import java.util.List;

public interface ClientDao {
    Client insert(Client client) throws OnlineShopException;

    Client getById(int id) throws OnlineShopException;

    List<Client> getAll() throws OnlineShopException;

    void update(Client client) throws OnlineShopException;

    void updateDeposit(Client client) throws OnlineShopException;

    void delete(Client client) throws OnlineShopException;

    void deleteAll() throws OnlineShopException;
}