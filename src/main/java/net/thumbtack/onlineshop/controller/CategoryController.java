package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.request.CategoryRequest;
import net.thumbtack.onlineshop.dto.request.UpdateCategoryRequest;
import net.thumbtack.onlineshop.dto.response.CategoryResponse;
import net.thumbtack.onlineshop.dto.response.EmptyResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.service.CategoryService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CategoryResponse create(@CookieValue("JAVASESSIONID") String sessionId,
                                   @Valid @RequestBody CategoryRequest request) throws OnlineShopException {
        return categoryService.create(sessionId, request);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CategoryResponse get(@CookieValue("JAVASESSIONID") String sessionId,
                                @PathVariable(value = "id") int categoryId) throws OnlineShopException {
        return categoryService.get(sessionId, categoryId);
    }

    @GetMapping
    public List<CategoryResponse> getAll(@CookieValue("JAVASESSIONID") String sessionId) throws OnlineShopException {
        return categoryService.getAll(sessionId);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CategoryResponse update(@CookieValue("JAVASESSIONID") String sessionId,
                                   @PathVariable(value = "id") int categoryId,
                                   @RequestBody UpdateCategoryRequest request) throws OnlineShopException {
        return categoryService.update(sessionId, categoryId, request);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse delete(@CookieValue("JAVASESSIONID") String sessionId,
                                @PathVariable(value = "id") int categoryId) throws OnlineShopException {
        return categoryService.delete(sessionId, categoryId);
    }
}