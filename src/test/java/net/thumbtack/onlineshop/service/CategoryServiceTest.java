package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dao.CategoryDao;
import net.thumbtack.onlineshop.dto.request.CategoryRequest;
import net.thumbtack.onlineshop.dto.request.UpdateCategoryRequest;
import net.thumbtack.onlineshop.dto.response.CategoryResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Administrator;
import net.thumbtack.onlineshop.model.Category;
import net.thumbtack.onlineshop.model.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CategoryServiceTest extends BaseServiceTest {
    private Administrator admin = new Administrator("Sasha", "Michael", "DevOps", "SayHello", "12321qq");
    private Category parent = new Category(13, "parent", null, null, null);
    private Category newParent = new Category(333, "newParent", null, null, null);
    private Category child = new Category(666, "child", parent, null, null);

    @Mock
    CategoryDao categoryDao;

    @InjectMocks
    CategoryService categoryService;

    @Before
    public void setUp() throws OnlineShopException {
        MockitoAnnotations.initMocks(this);
        String sessionId = UUID.randomUUID().toString();
        when(categoryDao.getById(parent.getId())).thenReturn(parent);
        when(categoryDao.getById(child.getId())).thenReturn(child);
        when(categoryDao.getById(newParent.getId())).thenReturn(newParent);
        when(adminDao.getById(any(Integer.class))).thenReturn(admin);
        when(sessionDao.getById(any(String.class))).thenReturn(new Session(sessionId, admin));
    }

    @Test
    public void testCreateCategory() throws OnlineShopException {
        when(categoryDao.insert(any(Category.class))).thenReturn(parent);
        CategoryRequest request = new CategoryRequest("parent", null);
        CategoryResponse response = categoryService.create(UUID.randomUUID().toString(), request);
        assertEquals(parent.getName(), response.getName());
        assertNull(response.getParentId());
        assertNull(response.getParentName());
    }

    @Test
    public void testCreateSubcategory() throws OnlineShopException {
        when(categoryDao.insert(any(Category.class))).thenReturn(child);
        CategoryRequest request = new CategoryRequest("child", parent.getId());
        CategoryResponse response = categoryService.create(UUID.randomUUID().toString(), request);
        assertEquals(child.getName(), response.getName());
        assertEquals(parent.getId(), (int) response.getParentId());
        assertEquals(parent.getName(), response.getParentName());
    }

    @Test
    public void testUpdateCategory() throws OnlineShopException {
        UpdateCategoryRequest request = new UpdateCategoryRequest("newName");
        CategoryResponse response = categoryService.update(UUID.randomUUID().toString(), parent.getId(), request);
        assertEquals(request.getName(), response.getName());
    }

    @Test
    public void testUpdateCategoryToSubcategory() {
        UpdateCategoryRequest request = new UpdateCategoryRequest("newName", child.getId());
        try {
            categoryService.update(UUID.randomUUID().toString(), parent.getId(), request);
        } catch (OnlineShopException e) {
            assertEquals(OnlineShopErrorCode.CATEGORY_PARENT, e.getErrorCode());
        }
    }

    @Test
    public void testUpdateSubcategory() throws OnlineShopException {
        UpdateCategoryRequest request = new UpdateCategoryRequest("newChildName", newParent.getId());
        CategoryResponse response = categoryService.update(UUID.randomUUID().toString(), child.getId(), request);
        assertEquals(request.getName(), response.getName());
        assertEquals(newParent.getId(), (int) response.getParentId());
        assertEquals(newParent.getName(), response.getParentName());
    }

    @Test
    public void testUpdateSubcategoryToCategory() {
        UpdateCategoryRequest request = new UpdateCategoryRequest("newChildName", null);
        try {
            categoryService.update(UUID.randomUUID().toString(), child.getId(), request);
        } catch (OnlineShopException e) {
            assertEquals(OnlineShopErrorCode.CATEGORY_PARENT, e.getErrorCode());
        }
    }
}