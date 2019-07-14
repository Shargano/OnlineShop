package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dao.AdminDao;
import net.thumbtack.onlineshop.dao.CategoryDao;
import net.thumbtack.onlineshop.dao.ClientDao;
import net.thumbtack.onlineshop.dao.SessionDao;
import net.thumbtack.onlineshop.dto.request.CategoryRequest;
import net.thumbtack.onlineshop.dto.request.UpdateCategoryRequest;
import net.thumbtack.onlineshop.dto.response.CategoryResponse;
import net.thumbtack.onlineshop.dto.response.EmptyResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService extends ServiceBase {
    private final CategoryDao categoryDao;

    public CategoryService(SessionDao sessionDao, AdminDao adminDao, ClientDao clientDao, CategoryDao categoryDao) {
        super(sessionDao, adminDao, clientDao);
        this.categoryDao = categoryDao;
    }

    public CategoryResponse create(String sessionId, CategoryRequest request) throws OnlineShopException {
        checkUserIsAdmin(getUserFromDB(sessionId));
        Category parent = null;
        if (request.getParentId() != null && request.getParentId() != 0)
            parent = categoryDao.getById(request.getParentId());
        Category result = categoryDao.insert(new Category(request.getName(), parent));
        return new CategoryResponse(result.getId(), result.getName(), result.getParent());
    }

    public CategoryResponse get(String sessionId, int categoryId) throws OnlineShopException {
        Category category = getCategoryFromDB(sessionId, categoryId);
        return new CategoryResponse(category.getId(), category.getName(), category.getParent());
    }

    public List<CategoryResponse> getAll(String sessionId) throws OnlineShopException {
        checkUserIsAdmin(getUserFromDB(sessionId));
        List<Category> categories = categoryDao.getAll();
        List<CategoryResponse> responses = new ArrayList<>();
        categories.forEach(category -> responses.add(new CategoryResponse(category.getId(), category.getName(), category.getParent())));
        return responses;
    }

    public CategoryResponse update(String sessionId, int categoryId, UpdateCategoryRequest request) throws OnlineShopException {
        isOneMoreFieldSet(request);
        Category category = getCategoryFromDB(sessionId, categoryId);
        checkCategoryFields(category, request);
        if (request.getParentId() != null && !request.getParentId().equals(UpdateCategoryRequest.DEFAULT_PARENT)) {
            Category newParent = categoryDao.getById(request.getParentId());
            category.setParent(newParent);
        }
        if (request.getName() != null)
            category.setName(request.getName());
        categoryDao.update(category);
        return new CategoryResponse(category.getId(), category.getName(), category.getParent());
    }

    public EmptyResponse delete(String sessionId, int categoryId) throws OnlineShopException {
        Category category = getCategoryFromDB(sessionId, categoryId);
        categoryDao.delete(category);
        return new EmptyResponse();
    }

    private Category getCategoryFromDB(String sessionId, int categoryId) throws OnlineShopException {
        checkUserIsAdmin(getUserFromDB(sessionId));
        return categoryDao.getById(categoryId);
    }

    private void checkCategoryFields(Category category, UpdateCategoryRequest request) throws OnlineShopException {
        if (category.getParent() != null && request.getParentId() == null) // делаем из подкатегории категорию
            throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_PARENT);
        if (category.getParent() == null && (request.getParentId() == null || !request.getParentId().equals(UpdateCategoryRequest.DEFAULT_PARENT))) // делаем из категории подкатегорию
            throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_PARENT);
    }

    private void isOneMoreFieldSet(UpdateCategoryRequest request) throws OnlineShopException {
        if (request.getParentId() != null && request.getParentId().equals(UpdateCategoryRequest.DEFAULT_PARENT) && request.getName() == null)
            throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_FIELDS);
    }
}