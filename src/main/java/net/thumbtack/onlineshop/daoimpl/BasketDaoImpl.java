package net.thumbtack.onlineshop.daoimpl;

import net.thumbtack.onlineshop.dao.BasketDao;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Basket;
import net.thumbtack.onlineshop.model.BasketProductItem;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Product;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BasketDaoImpl extends DaoImplBase implements BasketDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasketDaoImpl.class);

    @Override
    public Basket insertProduct(Basket basket, Product product, Integer count) throws OnlineShopException {
        LOGGER.debug("DAO insert product {} into basket {}", product, basket);
        try (SqlSession sqlSession = getSession()) {
            try {
                BasketProductItem item = new BasketProductItem(product, count);
                getBasketProductItemMapper(sqlSession).insert(basket, item);
                basket.addItem(item);
            } catch (RuntimeException e) {
                LOGGER.error("Can't insert product into basket", e);
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return basket;
    }

    @Override
    public Basket getByClient(Client client) throws OnlineShopException {
        LOGGER.debug("DAO get basket by client, id = {}", client.getId());
        try (SqlSession sqlSession = getSession()) {
            try {
                Basket basket = getBasketMapper(sqlSession).getByClient(client);
                if (basket == null)
                    throw new OnlineShopException(OnlineShopErrorCode.BASKET_NOT_EXISTS);
                return basket;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get basket");
                e.printStackTrace();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public void deleteProductFromBasket(Client client, Product product) throws OnlineShopException {
        LOGGER.debug("DAO delete from basket product with Id = {}", product.getId());
        try (SqlSession sqlSession = getSession()) {
            try {
                getBasketMapper(sqlSession).deleteProduct(client, product);
            } catch (RuntimeException e) {
                LOGGER.error("Can't delete from basket product");
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void changeProductCount(Client client, BasketProductItem item) throws OnlineShopException {
        LOGGER.debug("DAO change in basket product {}", item.getProduct());
        try (SqlSession sqlSession = getSession()) {
            try {
                getBasketMapper(sqlSession).changeCount(client, item);
            } catch (RuntimeException e) {
                LOGGER.error("Can't change product count in basket");
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }
}