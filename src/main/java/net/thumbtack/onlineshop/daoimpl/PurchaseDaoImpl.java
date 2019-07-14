package net.thumbtack.onlineshop.daoimpl;

import net.thumbtack.onlineshop.dao.PurchaseDao;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Purchase;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PurchaseDaoImpl extends DaoImplBase implements PurchaseDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseDaoImpl.class);

    @Override
    public Purchase insert(Purchase purchase) throws OnlineShopException {
        LOGGER.debug("DAO insert purchase {}", purchase);
        try (SqlSession sqlSession = getSession()) {
            try {
                if (getClientMapper(sqlSession).updateDeposit(purchase.getClient()) == 0) {
                    sqlSession.rollback();
                    throw new OnlineShopException(OnlineShopErrorCode.DEPOSIT_WAS_CHANGED);
                }
                if (getProductMapper(sqlSession).update(purchase.getProduct()) == 0) {
                    sqlSession.rollback();
                    throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_WAS_CHANGED);
                }
                getPurchaseMapper(sqlSession).insert(purchase);
            } catch (RuntimeException e) {
                LOGGER.error("Can't insert purchase", e);
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return purchase;
    }

    @Override
    public List<Purchase> insertList(List<Purchase> purchases, Client client) throws OnlineShopException {
        LOGGER.debug("DAO insert purchases list");
        try (SqlSession sqlSession = getSession()) {
            try {
                if (getClientMapper(sqlSession).updateDeposit(purchases.get(0).getClient()) == 0) {
                    sqlSession.rollback();
                    throw new OnlineShopException(OnlineShopErrorCode.DEPOSIT_WAS_CHANGED);
                }
                for (Purchase purchase : purchases) {
                    if (getProductMapper(sqlSession).updateCount(purchase.getProduct()) == 0) {
                        sqlSession.rollback();
                        throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_WAS_CHANGED);
                    }
                }
                getPurchaseMapper(sqlSession).batchInsert(purchases);
                getBasketProductItemMapper(sqlSession).deleteAll(client.getBasket());
                if (client.getBasket().getItems().size() > 0)
                    getBasketProductItemMapper(sqlSession).insertList(client.getBasket(), client.getBasket().getItems());
            } catch (RuntimeException e) {
                LOGGER.error("Can't insert list purchases", e);
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return purchases;
    }

    @Override
    public Purchase getById(int id) {
        LOGGER.debug("DAO get purchase by Id = {}", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getPurchaseMapper(sqlSession).getById(id);
            } catch (RuntimeException e) {
                LOGGER.error("Can't get purchase");
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public List<Client> getAllByClients(List<Integer> clients, Integer offset, Integer limit, String order) throws OnlineShopException {
        LOGGER.debug("DAO get all clients");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getPurchaseMapper(sqlSession).getAllByClients(clients, offset, limit, order);
            } catch (RuntimeException e) {
                LOGGER.error("Can't get all clients");
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public List<Purchase> getAllByProducts(List<Integer> products, Integer offset, Integer limit, String order) throws OnlineShopException {
        LOGGER.debug("DAO get all products");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getPurchaseMapper(sqlSession).getAllByProducts(products, offset, limit, order);
            } catch (RuntimeException e) {
                LOGGER.error("Can't get all products");
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public List<Purchase> getAllByCategories(List<Integer> categories, Integer offset, Integer limit, String order) throws OnlineShopException {
        LOGGER.debug("DAO get all categories");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getPurchaseMapper(sqlSession).getAllByCategories(categories, offset, limit, order);
            } catch (RuntimeException e) {
                LOGGER.error("Can't get all categories");
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public void delete(Purchase purchase) throws OnlineShopException {
        LOGGER.debug("DAO delete purchase {}", purchase);
        try (SqlSession sqlSession = getSession()) {
            try {
                getPurchaseMapper(sqlSession).delete(purchase);
            } catch (RuntimeException e) {
                LOGGER.error("Can't delete purchase");
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }
}