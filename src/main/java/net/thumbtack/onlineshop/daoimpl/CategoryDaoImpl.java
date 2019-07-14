package net.thumbtack.onlineshop.daoimpl;

import net.thumbtack.onlineshop.dao.CategoryDao;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Category;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryDaoImpl extends DaoImplBase implements CategoryDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryDaoImpl.class);

    @Override
    public Category insert(Category category) throws OnlineShopException {
        LOGGER.debug("DAO insert category {}", category);
        try (SqlSession sqlSession = getSession()) {
            try {
                getCategoryMapper(sqlSession).insert(category);
            } catch (RuntimeException e) {
                LOGGER.error("Can't insert category");
                sqlSession.rollback();
                checkDuplicate(e, OnlineShopErrorCode.CATEGORY_ALREADY_EXISTS);
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return category;
    }

    @Override
    public Category getById(int id) throws OnlineShopException {
        LOGGER.debug("DAO get category by Id = {}", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                Category category = getCategoryMapper(sqlSession).getById(id);
                if (category == null)
                    throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_NOT_EXISTS);
                return category;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get category", e);
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public List<Category> getByListOfId(List<Integer> idList) throws OnlineShopException {
        LOGGER.debug("DAO get categories with id = {}", idList);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getCategoryMapper(sqlSession).getByListOfId(idList);
            } catch (RuntimeException e) {
                LOGGER.error("Can't get list of categories");
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public List<Category> getAll() throws OnlineShopException {
        LOGGER.debug("DAO get all categories");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getCategoryMapper(sqlSession).getAll();
            } catch (RuntimeException e) {
                LOGGER.error("Can't get all categories");
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public void update(Category category) throws OnlineShopException {
        LOGGER.debug("DAO update category {}", category);
        try (SqlSession sqlSession = getSession()) {
            try {
                getCategoryMapper(sqlSession).update(category);
            } catch (RuntimeException e) {
                LOGGER.error("Can't update category");
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void delete(Category category) throws OnlineShopException {
        LOGGER.debug("DAO delete category {}", category);
        try (SqlSession sqlSession = getSession()) {
            try {
                getCategoryMapper(sqlSession).delete(category);
            } catch (RuntimeException e) {
                LOGGER.error("Can't delete category");
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void deleteAll() throws OnlineShopException {
        LOGGER.debug("DAO delete all categories");
        try (SqlSession sqlSession = getSession()) {
            try {
                getCategoryMapper(sqlSession).deleteAll();
            } catch (RuntimeException e) {
                LOGGER.error("Can't delete all categories");
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

}