package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dao.*;
import net.thumbtack.onlineshop.dto.request.PurchaseProductRequest;
import net.thumbtack.onlineshop.dto.response.BuyBasketResponse;
import net.thumbtack.onlineshop.dto.response.PurchaseResponse;
import net.thumbtack.onlineshop.dto.response.account.ClientAccountInfoResponse;
import net.thumbtack.onlineshop.dto.response.report.*;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService extends ServiceBase {
    private final PurchaseDao purchaseDao;
    private final ProductDao productDao;

    public PurchaseService(SessionDao sessionDao, AdminDao adminDao, ClientDao clientDao, PurchaseDao purchaseDao, ProductDao productDao) {
        super(sessionDao, adminDao, clientDao);
        this.purchaseDao = purchaseDao;
        this.productDao = productDao;
    }

    public PurchaseResponse buyProduct(String sessionId, PurchaseProductRequest request) throws OnlineShopException {
        Client client = getClientFromDB(sessionId);
        Product product = productDao.getById(request.getId(), true);
        checkProduct(client, product, request);
        product.reduceCount(request.getCount());
        client.reduceDeposit(request.getPrice() * request.getCount());
        Purchase purchase = purchaseDao.insert(new Purchase(client, product, request.getCount()));
        return new PurchaseResponse(purchase.getProduct().getId(), purchase.getProductName(), purchase.getProductPrice(), purchase.getCount());
    }

    public BuyBasketResponse buyBasket(String sessionId, List<PurchaseProductRequest> requests) throws OnlineShopException {
        Client client = getClientFromDB(sessionId);
        List<Purchase> bought = new ArrayList<>();
        List<Purchase> remaining = new ArrayList<>();
        Product productFromDB;
        for (PurchaseProductRequest request : requests) {
            productFromDB = productDao.getById(request.getId(), false);
            if (!checkBasketProduct(client, productFromDB, request)) {
                if (request.getCount() == null)
                    request.setCount(client.getItemFromBasket(request.getId()).getCount());
                remaining.add(new Purchase(client, productFromDB, request.getName(), request.getPrice(), request.getCount()));
                continue;
            }
            adjustCount(client, request);
            if (request.getCount() > productFromDB.getCount()) {
                remaining.add(new Purchase(client, productFromDB, request.getCount()));
                continue;
            }
            productFromDB.reduceCount(request.getCount());
            bought.add(new Purchase(productFromDB.getId(), client, productFromDB, request.getCount()));
            client.adjustProductInBasket(productFromDB, request.getCount()); // корректировка корзины
            BasketProductItem item = client.getItemFromBasket(productFromDB.getId());
            if (item != null) // куплены не все единицы товара из корзины
                remaining.add(new Purchase(productFromDB.getId(), client, productFromDB, item.getCount()));
        }
        if (bought.size() == 0)
            return makeBasketResponse(bought, remaining);
        int totalCost = calculateCost(bought);
        if (totalCost > client.getDeposit().getValue()) {
            moveElements(bought, remaining);
            return makeBasketResponse(bought, remaining);
        }
        client.reduceDeposit(totalCost);
        purchaseDao.insertList(bought, client);
        return makeBasketResponse(bought, remaining);
    }

    public ReportResponse getReport(String sessionId, List<Integer> clients, List<Integer> products, List<Integer> categories,
                                    Integer offset, Integer limit, String order, Boolean onlyTotal) throws OnlineShopException {
        checkUserIsAdmin(getUserFromDB(sessionId));
        if (products != null) { // если список пустой - то по всем продуктам
            List<Purchase> purchases = purchaseDao.getAllByProducts(products, offset, limit, order);
            return makeProductReports(purchases, onlyTotal);
        }
        if (categories != null) { // если список пустой - то по всем категориям
            List<Purchase> purchases = purchaseDao.getAllByCategories(categories, offset, limit, order);
            return makeCategoryReports(purchases, onlyTotal);
        }
        List<Client> clientsFromDB = purchaseDao.getAllByClients(clients, offset, limit, order);
        return makeClientReports(clientsFromDB, onlyTotal);
    }

    private void checkProduct(Client client, Product product, PurchaseProductRequest request) throws OnlineShopException {
        if (!product.getName().equals(request.getName()))
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NAME_INCORRECT);
        if (product.getPrice() != request.getPrice())
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_PRICE_INCORRECT);
        if (product.getCount() < request.getCount())
            throw new OnlineShopException(OnlineShopErrorCode.NOT_ENOUGH_PRODUCT_COUNT);
        if (request.getCount() * request.getPrice() > client.getDeposit().getValue())
            throw new OnlineShopException(OnlineShopErrorCode.NOT_ENOUGH_MONEY);
    }

    private boolean checkBasketProduct(Client client, Product product, PurchaseProductRequest request) {
        if (product == null)
            return false;
        if (!product.getName().equals(request.getName()))
            return false;
        if (product.getPrice() != request.getPrice())
            return false;
        if (client.getProductFromBasket(product.getId()) == null)
            return false;
        return true;
    }

    private BuyBasketResponse makeBasketResponse(List<Purchase> bought, List<Purchase> remaining) {
        List<PurchaseResponse> boughtResponses = new ArrayList<>();
        List<PurchaseResponse> remainingResponses = new ArrayList<>();
        for (Purchase purchase : bought)
            boughtResponses.add(new PurchaseResponse(purchase.getId(), purchase.getProductName(), purchase.getProductPrice(), purchase.getCount()));
        for (Purchase purchase : remaining)
            remainingResponses.add(new PurchaseResponse(purchase.getId(), purchase.getProductName(), purchase.getProductPrice(), purchase.getCount()));
        return new BuyBasketResponse(boughtResponses, remainingResponses);
    }

    private void adjustCount(Client client, PurchaseProductRequest request) {
        int productCountInBasket = client.getItemFromBasket(request.getId()).getCount();
        if (request.getCount() == null || request.getCount() > productCountInBasket)
            request.setCount(productCountInBasket);
    }

    private int calculateCost(List<Purchase> purchases) {
        int totalCost = 0;
        for (Purchase purchase : purchases)
            totalCost += purchase.getCount() * purchase.getProductPrice();
        return totalCost;
    }

    private void moveElements(List<Purchase> from, List<Purchase> to) {
        to.addAll(from);
        from.clear();
    }

    private ReportResponse makeClientReports(List<Client> clients, Boolean onlyTotal) {
        int totalCost = 0;
        int costOfPurchases = 0;
        List<ClientReport> items = new ArrayList<>();
        for (Client client : clients) {
            List<PurchaseResponse> purchases = new ArrayList<>();
            for (Purchase purchase : client.getPurchases()) {
                costOfPurchases += purchase.getProductPrice() * purchase.getCount();
                if (onlyTotal) continue;
                purchases.add(new PurchaseResponse(purchase.getId(), purchase.getProductName(), purchase.getProductPrice(), purchase.getCount()));
            }
            totalCost += costOfPurchases;
            if (!onlyTotal)
                items.add(new ClientReport(client.getId(), client.getFirstName(), client.getLastName(), client.getPatronymic(), client.getEmail(), client.getAddress(), client.getPhone(), costOfPurchases, purchases));
            costOfPurchases = 0;
        }
        return new ClientReportResponse(totalCost, items);
    }

    private ReportResponse makeProductReports(List<Purchase> purchases, Boolean onlyTotal) {
        int totalCount = 0;
        List<ProductReport> items = new ArrayList<>();
        ClientAccountInfoResponse clientResponse;
        Client client;
        Product product;
        for (Purchase purchase : purchases) {
            totalCount += purchase.getCount();
            if (onlyTotal) continue;
            client = purchase.getClient();
            product = purchase.getProduct();
            clientResponse = new ClientAccountInfoResponse(client.getId(), client.getFirstName(), client.getLastName(), client.getPatronymic(), client.getEmail(), client.getAddress(), client.getPhone(), null);
            items.add(new ProductReport(purchase.getId(), product == null ? null : product.getId(), purchase.getProductName(), purchase.getProductPrice(), purchase.getCount(), clientResponse));
        }
        return new ProductReportResponse(totalCount, items);
    }

    private ReportResponse makeCategoryReports(List<Purchase> purchases, Boolean onlyTotal) {
        int totalCount = 0;
        Integer categoryId = null;
        String name = null;
        String parentName = null;
        Category category;
        List<CategoryReport> items = new ArrayList<>();
        for (Purchase purchase : purchases) {
            totalCount += purchase.getCount();
            if (onlyTotal) continue;
            Product product = purchase.getProduct();
            if (product != null && product.getCategories().size() != 0) {
                category = product.getCategories().get(product.getCategories().size() - 1); // последняя по иерархии категория
                categoryId = category.getId();
                name = category.getName();
                if (category.getParent() != null)
                    parentName = category.getParent().getName();
            }
            Client client = purchase.getClient();
            ClientAccountInfoResponse clientResponse = new ClientAccountInfoResponse(client.getId(), client.getFirstName(), client.getLastName(), client.getPatronymic(), client.getEmail(), client.getAddress(), client.getPhone(), null);
            PurchaseResponse purchaseResponse = new PurchaseResponse(purchase.getId(), purchase.getProductName(), purchase.getProductPrice(), purchase.getCount());
            items.add(new CategoryReport(categoryId, name, parentName, clientResponse, purchaseResponse));

        }
        return new CategoryReportResponse(totalCount, items);
    }
}