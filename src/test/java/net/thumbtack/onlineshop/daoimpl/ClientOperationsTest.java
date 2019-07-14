package net.thumbtack.onlineshop.daoimpl;

import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Client;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ClientOperationsTest extends BaseTest {
    @Test
    public void testInsertClient() throws OnlineShopException {
        Client client = insertClient("Danil", "Misnyk", "Dantes55@mail.ru", "omsk", "88005553535", "Dantesss", "12321qq");
        Client clientFromDB = clientDao.getById(client.getId());
        assertEquals(client, clientFromDB);
    }

    @Test(expected = OnlineShopException.class)
    public void testGetNonexistentClient() throws OnlineShopException {
        assertNull(clientDao.getById(123456137));
    }

    @Test(expected = OnlineShopException.class)
    public void testInsertClientWithNullName() throws OnlineShopException {
        Client client = new Client(null, "Misnyk", "Dantes55@mail.ru", "omsk", "88005553535", "Dantesss", "12321qq");
        clientDao.insert(client);
    }

    @Test
    public void testInsertClientWithoutPatronymic() throws OnlineShopException {
        Client client = new Client("Danil", "Misnyk", "Dantes55@mail.ru", "omsk", "88005553535", "Dantesss", "12321qq");
        clientDao.insert(client);
        Client clientFromDB = clientDao.getById(client.getId());
        assertEquals(client, clientFromDB);
    }

    @Test
    public void testInsertClient_DefaultDeposit() throws OnlineShopException {
        Client client = insertClient("Danil", "Misnyk", "Dantes55@mail.ru", "omsk", "88005553535", "Dantesss", "12321qq");
        Client clientFromDB = clientDao.getById(client.getId());
        assertEquals(client, clientFromDB);
        assertEquals(0, client.getDeposit().getValue());
    }

    @Test
    public void testUpdateClient() throws OnlineShopException {
        Client client = insertClient("Danil", "Misnyk", "Dantes55@mail.ru", "omsk", "88005553535", "Dantesss", "12321qq");
        Client clientFromDB = clientDao.getById(client.getId());
        assertEquals(client, clientFromDB);
        client.setFirstName("Michael");
        clientDao.update(client);
        clientFromDB = clientDao.getById(client.getId());
        assertEquals(client, clientFromDB);
    }

    @Test(expected = OnlineShopException.class)
    public void testUpdateClientSetNullName() throws OnlineShopException {
        Client client = insertClient("Danil", "Misnyk", "Dantes55@mail.ru", "omsk", "88005553535", "Dantesss", "12321qq");
        Client clientFromDB = clientDao.getById(client.getId());
        assertEquals(client, clientFromDB);
        client.setFirstName(null);
        clientDao.update(client);
    }

    @Test
    public void testPutOnDepositClient() throws OnlineShopException {
        Client client = insertClient("Danil", "Misnyk", "Dantes55@mail.ru", "omsk", "88005553535", "Dantesss", "12321qq");
        client.getDeposit().add(100);
        clientDao.updateDeposit(client);
        Client clientFromDB = clientDao.getById(client.getId());
        assertNotEquals(client.getDeposit(), clientFromDB.getDeposit());
        assertEquals(100, clientFromDB.getDeposit().getValue());
        assertEquals(2, clientFromDB.getDeposit().getVersion());
    }

    @Test(expected = OnlineShopException.class)
    public void testDeleteClient() throws OnlineShopException {
        Client client = insertClient("Danil", "Misnyk", "Dantes55@mail.ru", "omsk", "88005553535", "Dantesss", "12321qq");
        Client clientFromDB = clientDao.getById(client.getId());
        assertEquals(client, clientFromDB);
        clientDao.delete(client);
        clientDao.getById(client.getId());
    }

    @Test
    public void testInsertAndDeleteTwoClients() throws OnlineShopException {
        Client client1 = insertClient("Danil", "Misnyk", "Dantes55@mail.ru", "omsk", "88005553535", "Dantesss", "12321qq");
        Client client2 = insertClient("Michael", "Some", "AwesomeMail@mail.ru", "omsk", "88002000122", "YourBro", "12321qq");
        List<Client> clients = new ArrayList<>();
        clients.add(client1);
        clients.add(client2);
        List<Client> clientsFromDB = clientDao.getAll();
        assertEquals(clients, clientsFromDB);
        clientDao.deleteAll();
        clientsFromDB = clientDao.getAll();
        assertEquals(0, clientsFromDB.size());
    }
}