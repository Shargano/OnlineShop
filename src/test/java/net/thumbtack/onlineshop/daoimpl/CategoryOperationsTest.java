package net.thumbtack.onlineshop.daoimpl;

import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Category;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CategoryOperationsTest extends BaseTest {
    @Test
    public void testInsertCategory() throws OnlineShopException {
        Category category = insertCategory("Cars");
        Category categoryFromDB = categoryDao.getById(category.getId());
        assertEquals(category.getId(), categoryFromDB.getId());
        assertEquals(category.getName(), categoryFromDB.getName());
        assertNull(categoryFromDB.getParent());
    }

    @Test(expected = OnlineShopException.class)
    public void testGetNonexistentCategory() throws OnlineShopException {
        assertNull(categoryDao.getById(123006137));
    }

    @Test(expected = OnlineShopException.class)
    public void testInsertCategoryWithNullName() throws OnlineShopException {
        Category category = new Category(null);
        categoryDao.insert(category);
    }

    @Test
    public void testInsertNestedCategories() throws OnlineShopException {
        Category cars = insertCategory("Cars");
        Category bmw = insertCategory("BMW", cars);

        Category bmwFromDB = categoryDao.getById(bmw.getId());
        Category carsFromDB = categoryDao.getById(cars.getId());

        assertNotNull(bmwFromDB);
        assertNotNull(bmwFromDB.getParent());
        assertEquals(bmwFromDB.getParent().getId(), carsFromDB.getId());
        assertEquals(bmwFromDB.getParent().getName(), carsFromDB.getName());
    }

    @Test
    public void testUpdateCategory() throws OnlineShopException {
        Category category = insertCategory("Cars");
        Category categoryFromDB = categoryDao.getById(category.getId());
        assertEquals(category.getId(), categoryFromDB.getId());
        assertEquals(category.getName(), categoryFromDB.getName());
        category.setName("Planes");
        categoryDao.update(category);
        categoryFromDB = categoryDao.getById(category.getId());
        assertEquals(category.getId(), categoryFromDB.getId());
        assertEquals(category.getName(), categoryFromDB.getName());
    }

    @Test(expected = OnlineShopException.class)
    public void testUpdateCategorySetNullName() throws OnlineShopException {
        Category category = insertCategory("Cars");
        Category categoryFromDB = categoryDao.getById(category.getId());
        assertEquals(category.getId(), categoryFromDB.getId());
        assertEquals(category.getName(), categoryFromDB.getName());
        assertEquals(category.getParent(), categoryFromDB.getParent());
        category.setName(null);
        categoryDao.update(category);
    }

    @Test(expected = OnlineShopException.class)
    public void testDeleteCategory() throws OnlineShopException {
        Category category = insertCategory("Cars");
        Category categoryFromDB = categoryDao.getById(category.getId());
        assertEquals(category.getId(), categoryFromDB.getId());
        assertEquals(category.getName(), categoryFromDB.getName());
        categoryDao.delete(category);
        categoryDao.getById(category.getId());
    }

    @Test
    public void testInsertAndDeleteTwoCategories() throws OnlineShopException {
        Category category1 = insertCategory("Cars");
        Category category2 = insertCategory("Planes");
        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);
        List<Category> categoriesFromDB = categoryDao.getAll();
        assertEquals(2, categoriesFromDB.size());
        categoryDao.deleteAll();
        categoriesFromDB = categoryDao.getAll();
        assertEquals(0, categoriesFromDB.size());
    }

}