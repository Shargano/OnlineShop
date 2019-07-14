package net.thumbtack.onlineshop.daoimpl;

import net.thumbtack.onlineshop.dao.*;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.*;
import net.thumbtack.onlineshop.utils.MyBatisUtils;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class BaseTest {
    private CommonDao commonDao = new CommonDaoImpl();
    protected AdminDao adminDao = new AdminDaoImpl();
    protected ClientDao clientDao = new ClientDaoImpl();
    protected CategoryDao categoryDao = new CategoryDaoImpl();
    protected ProductDao productDao = new ProductDaoImpl();
    protected BasketDao basketDao = new BasketDaoImpl();
    protected PurchaseDao purchaseDao = new PurchaseDaoImpl();

    private static boolean setUpIsDone = false;

    @BeforeClass()
    public static void setUp() {
        if (!setUpIsDone) {
            Assume.assumeTrue(MyBatisUtils.initSqlSessionFactory());
            setUpIsDone = true;
        }
    }

    @Before
    public void clearDatabase() throws OnlineShopException {
        commonDao.clear();
    }

    protected Administrator insertAdmin(String firstName, String lastName, String position, String login, String password) throws OnlineShopException {
        Administrator admin = new Administrator(firstName, lastName, position, login, password);
        adminDao.insert(admin);
        assertNotEquals(0, admin.getId());
        return admin;
    }

    protected Client insertClient(String firstName, String lastName, String email, String address, String phone, String login, String password) throws OnlineShopException {
        Client client = new Client(firstName, lastName, email, address, phone, login, password);
        clientDao.insert(client);
        assertNotEquals(0, client.getId());
        return client;
    }

    protected Category insertCategory(String name, Category parent) throws OnlineShopException {
        Category category = new Category(name, parent);
        categoryDao.insert(category);
        assertNotEquals(0, category.getId());
        return category;
    }

    protected Category insertCategory(String name) throws OnlineShopException {
        Category category = new Category(name);
        categoryDao.insert(category);
        assertNotEquals(0, category.getId());
        return category;
    }

    protected Product insertProduct(String name, int price) throws OnlineShopException {
        Product product = new Product(name, price);
        productDao.insert(product);
        assertNotEquals(0, product.getId());
        return product;
    }

    protected Product insertProduct(String name, int price, int count) throws OnlineShopException {
        Product product = new Product(name, price, count);
        productDao.insert(product);
        assertNotEquals(0, product.getId());
        return product;
    }

    protected Basket insertBasket(Client client, Product product, int count) throws OnlineShopException {
        basketDao.insertProduct(client.getBasket(), product, count);
        assertNotNull(client.getBasket());
        return client.getBasket();
    }

    protected Purchase insertPurchase(Client client, Product product, int count) throws OnlineShopException {
        Purchase purchase = new Purchase(client, product, count);
        purchaseDao.insert(purchase);
        assertNotEquals(0, purchase.getId());
        return purchase;
    }
}