package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dao.*;
import net.thumbtack.onlineshop.dto.request.ProductRequest;
import net.thumbtack.onlineshop.dto.request.UpdateProductRequest;
import net.thumbtack.onlineshop.dto.response.EmptyResponse;
import net.thumbtack.onlineshop.dto.response.ProductResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Category;
import net.thumbtack.onlineshop.model.Order;
import net.thumbtack.onlineshop.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService extends ServiceBase {
    private final ProductDao productDao;
    private final CategoryDao categoryDao;

    @Autowired
    public ProductService(SessionDao sessionDao, AdminDao adminDao, ClientDao clientDao, ProductDao productDao, CategoryDao categoryDao) {
        super(sessionDao, adminDao, clientDao);
        this.productDao = productDao;
        this.categoryDao = categoryDao;
    }

    public ProductResponse create(String sessionId, ProductRequest request) throws OnlineShopException {
        checkUserIsAdmin(getUserFromDB(sessionId));
        List<Category> categories = new ArrayList<>();
        if (request.getCategories() != null && request.getCategories().size() != 0)
            categories = getByListOfId(request.getCategories());
        Product product = productDao.insert(new Product(request.getName(), request.getPrice(), request.getCount(), categories));
        return makeResponse(product);
    }

    public ProductResponse get(String sessionId, int productId) throws OnlineShopException {
        getUserFromDB(sessionId);
        Product product = productDao.getById(productId, true);
        return makeResponse(product);
    }

    public ProductResponse update(String sessionId, int productId, UpdateProductRequest request) throws OnlineShopException {
        checkUserIsAdmin(getUserFromDB(sessionId));
        Product product = productDao.getById(productId, true);
        if (request.getName() != null)
            product.setName(request.getName());
        if (request.getPrice() != null)
            product.setPrice(request.getPrice());
        if (request.getCount() != null)
            product.setCount(request.getCount());
        if (request.getCategories() != null) {
            if (request.getCategories().size() == 0)
                product.setCategories(new ArrayList<>());
            else
                product.setCategories(getByListOfId(request.getCategories()));
        }
        productDao.update(product);
        return makeResponse(product);
    }

    public EmptyResponse delete(String sessionId, int productId) throws OnlineShopException {
        checkUserIsAdmin(getUserFromDB(sessionId));
        Product product = productDao.getById(productId, true);
        productDao.delete(product);
        return new EmptyResponse();
    }

    public List<ProductResponse> getAll(String sessionId, List<Integer> categories, String order) throws OnlineShopException {
        getUserFromDB(sessionId);
        List<Product> products;
        if (order.equals(Order.PRODUCT.getField())) {
            products = productDao.getAllOrderProduct(categories);
            return makeResponse(products);
        }
        if (order.equals(Order.CATEGORY.getField())) {
            products = productDao.getAllOrderCategory(categories);
            return makeResponse(products);
        }
        throw new OnlineShopException(OnlineShopErrorCode.WRONG_ORDER);
    }

    private ProductResponse makeResponse(Product product) {
        List<Integer> categoriesID = new ArrayList<>();
        if (product.getCategories() != null)
            product.getCategories().forEach(c -> categoriesID.add(c.getId()));
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount(), categoriesID);
    }

    private List<ProductResponse> makeResponse(List<Product> products) {
        List<ProductResponse> responses = new ArrayList<>();
        for (Product product : products)
            responses.add(makeResponse(product));
        return responses;
    }

    private List<Category> getByListOfId(List<Integer> categoriesId) throws OnlineShopException {
        List<Category> categories = categoryDao.getByListOfId(categoriesId);
        if (categories.size() != categoriesId.size())
            throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_NOT_EXISTS);
        return categories;
    }
}