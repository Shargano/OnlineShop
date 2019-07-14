package net.thumbtack.onlineshop.daoimpl;

import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Administrator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AdminOperationsTest extends BaseTest {
    @Test
    public void testInsertAdmin() throws OnlineShopException {
        Administrator admin = insertAdmin("Misha", "Dedun", "Leader", "mis", "123321qq");
        Administrator adminFromDB = adminDao.getById(admin.getId());
        assertEquals(admin, adminFromDB);
        assertEquals("Leader", adminFromDB.getPosition());
    }

    @Test(expected = OnlineShopException.class)
    public void testGetNonexistentAdmin() throws OnlineShopException {
        assertNull(adminDao.getById(123456137));
    }

    @Test(expected = OnlineShopException.class)
    public void testInsertAdminWithNullName() throws OnlineShopException {
        Administrator admin = new Administrator(null, "Misnyk", "Leonidovich", "leader", "Undead", "12321qq");
        adminDao.insert(admin);
    }

    @Test
    public void testInsertAdminWithoutPatronymic() throws OnlineShopException {
        Administrator admin = new Administrator("Daniil", "Misnyk", "leader", "BeenToHell", "12321qq");
        adminDao.insert(admin);
        Administrator adminFromDB = adminDao.getById(admin.getId());
        assertEquals(admin, adminFromDB);
    }

    @Test
    public void testUpdateAdmin() throws OnlineShopException {
        Administrator admin = insertAdmin("Misha", "Dedun", "Leader", "California", "123321qq");
        Administrator adminFromDB = adminDao.getById(admin.getId());
        assertEquals(admin, adminFromDB);
        admin.setFirstName("Michael");
        adminDao.update(admin);
        adminFromDB = adminDao.getById(admin.getId());
        assertEquals(admin, adminFromDB);
    }

    @Test(expected = OnlineShopException.class)
    public void testUpdateAdminSetNullName() throws OnlineShopException {
        Administrator admin = insertAdmin("Misha", "Dedun", "Leader", "WhateverItTakes", "123321qq");
        Administrator adminFromDB = adminDao.getById(admin.getId());
        assertEquals(admin, adminFromDB);
        admin.setFirstName(null);
        adminDao.update(admin);
    }

    @Test(expected = OnlineShopException.class)
    public void testDeleteAdmin() throws OnlineShopException {
        Administrator admin = insertAdmin("Misha", "Dedun", "Leader", "mis", "123321qq");
        Administrator adminFromDB = adminDao.getById(admin.getId());
        assertEquals(admin, adminFromDB);
        adminDao.delete(admin);
        adminDao.getById(admin.getId());
    }

}