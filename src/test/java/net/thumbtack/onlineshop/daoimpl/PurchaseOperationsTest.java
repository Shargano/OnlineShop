package net.thumbtack.onlineshop.daoimpl;

import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Product;
import net.thumbtack.onlineshop.model.Purchase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PurchaseOperationsTest extends BaseTest {
    @Test
    public void testInsertPurchase() throws OnlineShopException {
        Client client = insertClient("Danil", "Misnyk", "Dantes55@mail.ru", "omsk", "88005553535", "Dantesss", "12321qq");
        Product product = insertProduct("Audi", 12345, 100);
        Purchase purchase = insertPurchase(client, product, 12);
        Purchase purchaseFromDB = purchaseDao.getById(purchase.getId());
        assertEquals(purchase.getId(), purchaseFromDB.getId());
        assertEquals(purchase.getProductName(), purchaseFromDB.getProductName());
        assertEquals(purchase.getProductPrice(), purchaseFromDB.getProductPrice());
        assertEquals(purchase.getCount(), purchaseFromDB.getCount());
    }

    @Test(expected = OnlineShopException.class)
    public void testInsertPurchaseWithClientOutOfDatabase() throws OnlineShopException {
        Client client = new Client("Danil", "Misnyk", "Dantes55@mail.ru", "omsk", "88005553535", "Dantesss", "12321qq");
        Product product = insertProduct("Audi", 12345);
        insertPurchase(client, product, 69);
    }

    @Test(expected = OnlineShopException.class)
    public void testInsertPurchaseWithProductOutOfDatabase() throws OnlineShopException {
        Client client = insertClient("Danil", "Misnyk", "Dantes55@mail.ru", "omsk", "88005553535", "Dantesss", "12321qq");
        Product product = new Product("Audi", 12345);
        insertPurchase(client, product, 69);
    }

    @Test
    public void testInsertPurchase_DefaultCount() throws OnlineShopException {
        Client client = insertClient("Danil", "Misnyk", "Dantes55@mail.ru", "omsk", "88005553535", "Dantesss", "12321qq");
        Product product = insertProduct("Audi", 12345);

        Purchase purchase = new Purchase(client, product);
        purchaseDao.insert(purchase);

        Purchase purchaseFromDB = purchaseDao.getById(purchase.getId());
        assertEquals(purchase.getId(), purchaseFromDB.getId());
        assertEquals(purchase.getProductName(), purchaseFromDB.getProductName());
        assertEquals(purchase.getProductPrice(), purchaseFromDB.getProductPrice());
        assertEquals(purchase.getCount(), purchaseFromDB.getCount());
        assertEquals(1, purchaseFromDB.getCount());
    }

    @Test
    public void testDeletePurchase() throws OnlineShopException {
        Client client = insertClient("Danil", "Misnyk", "Dantes55@mail.ru", "omsk", "88005553535", "Dantesss", "12321qq");
        Product product = insertProduct("Audi", 12345);
        Purchase purchase = insertPurchase(client, product, 9);

        Purchase purchaseFromDB = purchaseDao.getById(purchase.getId());
        assertEquals(purchase.getId(), purchaseFromDB.getId());
        assertEquals(purchase.getProductName(), purchaseFromDB.getProductName());
        assertEquals(purchase.getProductPrice(), purchaseFromDB.getProductPrice());
        assertEquals(purchase.getCount(), purchaseFromDB.getCount());

        purchaseDao.delete(purchase);
        purchaseFromDB = purchaseDao.getById(purchase.getId());
        assertNull(purchaseFromDB);
    }

}