package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Purchase;

import java.util.List;

public interface PurchaseDao {
    Purchase insert(Purchase purchase) throws OnlineShopException;

    List<Purchase> insertList(List<Purchase> purchases, Client client) throws OnlineShopException;

    Purchase getById(int id);

    List<Client> getAllByClients(List<Integer> clients, Integer offset, Integer limit, String order) throws OnlineShopException;

    List<Purchase> getAllByProducts(List<Integer> products, Integer offset, Integer limit, String order) throws OnlineShopException;

    List<Purchase> getAllByCategories(List<Integer> categories, Integer offset, Integer limit, String order) throws OnlineShopException;

    void delete(Purchase purchase) throws OnlineShopException;
}