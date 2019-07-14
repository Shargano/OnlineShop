package net.thumbtack.onlineshop.daoimpl;

import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Category;
import net.thumbtack.onlineshop.model.Product;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProductOperationsTest extends BaseTest {
    @Test
    public void testInsertProduct() throws OnlineShopException {
        Product product = insertProduct("Audi", 12345);
        Product productFromDB = productDao.getById(product.getId(), true);
        assertEquals(product, productFromDB);
    }

    @Test(expected = OnlineShopException.class)
    public void testGetNonexistentProduct() throws OnlineShopException {
        assertNull(productDao.getById(123006137, true));
    }

    @Test(expected = OnlineShopException.class)
    public void testInsertProductWithNullName() throws OnlineShopException {
        Product product = new Product(null, 9);
        productDao.insert(product);
    }

    @Test
    public void testInsertProduct_DefaultCategoriesEmpty() throws OnlineShopException {
        Product product = insertProduct("Audi", 12345);
        Product productFromDB = productDao.getById(product.getId(), true);
        assertEquals(product, productFromDB);
        assertEquals(0, productFromDB.getCategories().size());
    }

    @Test
    public void testGetAllProductsOrderProduct1() throws OnlineShopException {
        Product product1 = insertProduct("books", 12345);
        Product product2 = insertProduct("apple", 12345);
        Category category1 = insertCategory("Vevo");
        Category category2 = insertCategory("Cars");
        List<Category> categories1 = new ArrayList<>();
        categories1.add(category1);
        Product product3 = productDao.insert(new Product("cheese", 12345, categories1));
        Product product4 = productDao.insert(new Product("zero", 12345, categories1));
        List<Category> categories2 = new ArrayList<>(categories1);
        categories2.add(category2);
        Product product5 = productDao.insert(new Product("Audi", 12345, categories2));

        List<Product> products = productDao.getAllOrderProduct(null);

        testProductFields(product2, products.get(0), true);
        testProductFields(product5, products.get(1), true);
        testProductFields(product1, products.get(2), true);
        testProductFields(product3, products.get(3), true);
        testProductFields(product4, products.get(4), true);
    }

    @Test
    public void testGetAllProductsOrderProduct2() throws OnlineShopException {
        Product product1 = insertProduct("books", 12345);
        Product product2 = insertProduct("apple", 12345);
        Category category1 = insertCategory("Vevo");
        Category category2 = insertCategory("Cars");
        List<Category> categories1 = new ArrayList<>();
        categories1.add(category1);
        Product product3 = productDao.insert(new Product("cheese", 12345, categories1));
        Product product4 = productDao.insert(new Product("zero", 12345, categories1));
        List<Category> categories2 = new ArrayList<>(categories1);
        categories2.add(category2);
        Product product5 = productDao.insert(new Product("Audi", 12345, categories2));

        List<Product> products = productDao.getAllOrderProduct(new ArrayList<>());

        assertEquals(2, products.size());
        testProductFields(product2, products.get(0), true);
        testProductFields(product1, products.get(1), true);
    }

    @Test
    public void testGetAllProductsOrderProduct3() throws OnlineShopException {
        Product product1 = insertProduct("books", 12345);
        Product product2 = insertProduct("apple", 12345);
        Category category1 = insertCategory("Vevo");
        Category category2 = insertCategory("Cars");
        List<Category> categories1 = new ArrayList<>();
        categories1.add(category1);
        Product product3 = productDao.insert(new Product("cheese", 12345, categories1));
        Product product4 = productDao.insert(new Product("zero", 12345, categories1));
        List<Category> categories2 = new ArrayList<>(categories1);
        categories2.add(category2);
        Product product5 = productDao.insert(new Product("Audi", 12345, categories2));

        List<Integer> list = new ArrayList<>();
        list.add(category1.getId());
        list.add(category2.getId());
        List<Product> products = productDao.getAllOrderProduct(list);

        assertEquals(3, products.size());
        testProductFields(product5, products.get(0), true);
        testProductFields(product3, products.get(1), true);
        testProductFields(product4, products.get(2), true);
    }

    @Test
    public void testGetAllProductsOrderCategory1() throws OnlineShopException {
        Product product1 = insertProduct("books", 12345);
        Product product2 = insertProduct("apple", 12345);
        Category category1 = insertCategory("Vevo");
        Category category2 = insertCategory("Cars");
        List<Category> categories1 = new ArrayList<>();
        categories1.add(category1);
        Product product3 = productDao.insert(new Product("cheese", 12345, categories1));
        Product product4 = productDao.insert(new Product("zero", 12345, categories1));
        List<Category> categories2 = new ArrayList<>(categories1);
        categories2.add(category2);
        Product product5 = productDao.insert(new Product("Audi", 12345, categories2));

        List<Product> products = productDao.getAllOrderCategory(null);

        assertEquals(6, products.size());
        testProductFields(product2, products.get(0), false);
        testProductFields(product1, products.get(1), false);
        testProductFields(product5, products.get(2), false);
        testCategoryFields(category2, products.get(2).getCategories().get(0));
        testProductFields(product5, products.get(3), false);
        testCategoryFields(category1, products.get(3).getCategories().get(0));
        testProductFields(product3, products.get(4), false);
        testCategoryFields(category1, products.get(4).getCategories().get(0));
        testProductFields(product4, products.get(5), false);
        testCategoryFields(category1, products.get(5).getCategories().get(0));
    }

    @Test
    public void testGetAllProductsOrderCategory2() throws OnlineShopException {
        Product product1 = insertProduct("books", 12345);
        Product product2 = insertProduct("apple", 12345);
        Category category1 = insertCategory("Vevo");
        Category category2 = insertCategory("Cars");
        List<Category> categories1 = new ArrayList<>();
        categories1.add(category1);
        Product product3 = productDao.insert(new Product("cheese", 12345, categories1));
        Product product4 = productDao.insert(new Product("zero", 12345, categories1));
        List<Category> categories2 = new ArrayList<>(categories1);
        categories2.add(category2);
        Product product5 = productDao.insert(new Product("Audi", 12345, categories2));

        List<Product> products = productDao.getAllOrderCategory(new ArrayList<>());

        assertEquals(2, products.size());
        testProductFields(product2, products.get(0), true);
        testProductFields(product1, products.get(1), true);
    }

    @Test
    public void testGetAllProductsOrderCategory3() throws OnlineShopException {
        Product product1 = insertProduct("books", 12345);
        Product product2 = insertProduct("apple", 12345);
        Category category1 = insertCategory("Vevo");
        Category category2 = insertCategory("Cars");
        List<Category> categories1 = new ArrayList<>();
        categories1.add(category1);
        Product product3 = productDao.insert(new Product("cheese", 12345, categories1));
        Product product4 = productDao.insert(new Product("zero", 12345, categories1));
        List<Category> categories2 = new ArrayList<>(categories1);
        categories2.add(category2);
        Product product5 = productDao.insert(new Product("Audi", 12345, categories2));

        List<Integer> list = new ArrayList<>();
        list.add(category1.getId());
        list.add(category2.getId());
        List<Product> products = productDao.getAllOrderCategory(list);

        assertEquals(4, products.size());
        testProductFields(product5, products.get(0), false);
        testCategoryFields(category2, products.get(0).getCategories().get(0));
        testProductFields(product5, products.get(1), false);
        testCategoryFields(category1, products.get(1).getCategories().get(0));
        testProductFields(product3, products.get(2), false);
        testCategoryFields(category1, products.get(2).getCategories().get(0));
        testProductFields(product4, products.get(3), false);
        testCategoryFields(category1, products.get(3).getCategories().get(0));
    }

    @Test
    public void testInsertProductWithCategory() throws OnlineShopException {
        Category category = insertCategory("Cars");
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        Product product = new Product("Audi", 12345);
        product.setCategories(categories);

        productDao.insert(product);
        Product productFromDB = productDao.getById(product.getId(), true);
        assertEquals(product.getId(), productFromDB.getId());
        assertEquals(product.getName(), productFromDB.getName());
        assertEquals(product.getPrice(), productFromDB.getPrice());

        assertEquals(product.getCategories().get(0).getId(), category.getId());
        assertEquals(product.getCategories().get(0).getName(), category.getName());
    }

    @Test
    public void testUpdateProduct() throws OnlineShopException {
        Product product = insertProduct("Audi", 12345);
        Product productFromDB = productDao.getById(product.getId(), true);
        assertEquals(product, productFromDB);
        product.setCount(999);
        product.setName("BMW");
        productDao.update(product);
        productFromDB = productDao.getById(product.getId(), true);
        assertEquals(2, productFromDB.getVersion());
    }

    @Test
    public void testUpdateProductWithCategories() throws OnlineShopException {
        Product product = insertProduct("Audi Model S", 12345);
        Category cars = insertCategory("Cars");
        Category audi = insertCategory("Audi", cars);
        List<Category> categories = new ArrayList<>();
        categories.add(cars);
        categories.add(audi);
        product.setCategories(categories);

        productDao.update(product);
        Product productFromDB = productDao.getById(product.getId(), true);

        assertEquals(product.getId(), productFromDB.getId());
        assertEquals(product.getName(), productFromDB.getName());
        assertEquals(product.getPrice(), productFromDB.getPrice());

        assertEquals(2, product.getCategories().size());
    }

    // checkCategory отвечает за проверку категорий, в сортировке по категориям нельзя сравнивать весь список категорий
    private void testProductFields(Product expected, Product actual, boolean checkCategory) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getCount(), actual.getCount());
        if (expected.getCategories() != null && expected.getCategories().size() > 0 && checkCategory) {
            for (int i = 0; i < expected.getCategories().size(); i++)
                testCategoryFields(expected.getCategories().get(i), actual.getCategories().get(i));
        }
    }

    private void testCategoryFields(Category expected, Category actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        if (expected.getParent() != null) {
            assertEquals(expected.getParent().getId(), actual.getParent().getId());
            assertEquals(expected.getParent().getName(), actual.getParent().getName());
        }
    }
}
