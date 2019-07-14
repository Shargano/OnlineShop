package net.thumbtack.onlineshop.daoimpl;

import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Basket;
import net.thumbtack.onlineshop.model.BasketProductItem;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Product;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BasketOperationsTest extends BaseTest {
    @Test
    public void testInsertIntoBasket() throws OnlineShopException {
        Client client = insertClient("Danil", "Misnyk", "Dantes55@mail.ru", "omsk", "88005553535", "Dantesss", "12321qq");
        Product product = insertProduct("Audi", 12345);
        Basket basket = insertBasket(client, product, 69);
        Basket basketFromDB = basketDao.getByClient(client);
        assertEquals(basket, basketFromDB);
    }

    @Test(expected = OnlineShopException.class)
    public void testInsertBasketWithClientOutOfDatabase() throws OnlineShopException {
        Client client = new Client("Danil", "Misnyk", "Dantes55@mail.ru", "omsk", "88005553535", "Dantesss", "12321qq");
        Product product = insertProduct("Audi", 12345);
        insertBasket(client, product, 69);
    }

    @Test(expected = OnlineShopException.class)
    public void testInsertBasketWithProductOutOfDatabase() throws OnlineShopException {
        Client client = insertClient("Danil", "Misnyk", "Dantes55@mail.ru", "omsk", "88005553535", "Dantesss", "12321qq");
        Product product = new Product("Audi", 12345);
        insertBasket(client, product, 69);
    }

    @Test
    public void testInsertBasket_DefaultCount() throws OnlineShopException {
        Client client = insertClient("Danil", "Misnyk", "Dantes55@mail.ru", "omsk", "88005553535", "Dantesss", "12321qq");
        Product product = insertProduct("Audi", 12345);

        BasketProductItem item = new BasketProductItem(product);
        basketDao.insertProduct(client.getBasket(), item.getProduct(), item.getCount());

        Basket basketFromDB = basketDao.getByClient(client);
        assertEquals(client.getBasket(), basketFromDB);
        assertEquals(1, basketFromDB.getItems().get(0).getCount());
    }

    @Test
    public void testDeleteProductFromBasket() throws OnlineShopException {
        Client client = insertClient("Danil", "Misnyk", "Dantes55@mail.ru", "omsk", "88005553535", "Dantesss", "12321qq");
        Product product = insertProduct("Audi", 12345);
        Basket basket = insertBasket(client, product, 9);

        Basket basketFromDB = basketDao.getByClient(client);
        assertEquals(basket, basketFromDB);

        basketDao.deleteProductFromBasket(client, product);
        basketFromDB = basketDao.getByClient(client);
        assertEquals(0, basketFromDB.getItems().size());
    }


}