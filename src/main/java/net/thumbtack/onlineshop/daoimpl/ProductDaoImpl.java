package net.thumbtack.onlineshop.daoimpl;

import net.thumbtack.onlineshop.dao.ProductDao;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Product;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDaoImpl extends DaoImplBase implements ProductDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDaoImpl.class);

    @Override
    public Product insert(Product product) throws OnlineShopException {
        LOGGER.debug("DAO insert product {}", product);
        try (SqlSession sqlSession = getSession()) {
            try {
                getProductMapper(sqlSession).insert(product);
                if (product.getCategories().size() != 0) {
                    getProductMapper(sqlSession).addProductToCategories(product, product.getCategories());
                }
            } catch (RuntimeException e) {
                LOGGER.error("Can't insert product");
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return product;
    }

    @Override
    public Product getById(int id, boolean throwExceptionIfNotFound) throws OnlineShopException {
        LOGGER.debug("DAO get product by Id = {}", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                Product product = getProductMapper(sqlSession).getById(id);
                if (product == null && throwExceptionIfNotFound)
                    throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_EXISTS);
                return product;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get product", e);
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public List<Product> getAllOrderProduct(List<Integer> categories) throws OnlineShopException {
        LOGGER.debug("DAO get all products order by product with categories {}", categories);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getProductMapper(sqlSession).getAllOrderByProduct(categories);
            } catch (RuntimeException e) {
                LOGGER.error("Can't get all products order by product");
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public List<Product> getAllOrderCategory(List<Integer> categories) throws OnlineShopException {
        LOGGER.debug("DAO get all products order by category with categories {}", categories);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getProductMapper(sqlSession).getAllOrderByCategory(categories);
            } catch (RuntimeException e) {
                LOGGER.error("Can't get all products order by category");
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public void update(Product product) throws OnlineShopException {
        LOGGER.debug("DAO update product {}", product);
        try (SqlSession sqlSession = getSession()) {
            try {
                int result = getProductMapper(sqlSession).update(product);
                if (result == 0) {
                    sqlSession.rollback();
                    throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_WAS_CHANGED);
                }
                if (product.getCategories().size() == 0) {
                    getProductMapper(sqlSession).deleteCategories(product);
                }
                if (product.getCategories().size() != 0) {
                    getProductMapper(sqlSession).deleteCategories(product);
                    getProductMapper(sqlSession).addProductToCategories(product, product.getCategories());
                }
            } catch (RuntimeException e) {
                LOGGER.error("Can't update product");
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void delete(Product product) throws OnlineShopException {
        LOGGER.debug("DAO delete product {}", product);
        try (SqlSession sqlSession = getSession()) {
            try {
                getProductMapper(sqlSession).delete(product);
            } catch (RuntimeException e) {
                LOGGER.error("Can't delete product");
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }
}