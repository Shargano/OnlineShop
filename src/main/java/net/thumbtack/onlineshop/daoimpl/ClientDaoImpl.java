package net.thumbtack.onlineshop.daoimpl;

import net.thumbtack.onlineshop.dao.ClientDao;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Client;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientDaoImpl extends DaoImplBase implements ClientDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientDaoImpl.class);

    @Override
    public Client insert(Client client) throws OnlineShopException {
        LOGGER.debug("DAO insert client {}", client);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insert(client);
                getClientMapper(sqlSession).insert(client);
                getClientMapper(sqlSession).createDeposit(client);
                getClientMapper(sqlSession).createBasket(client);
            } catch (RuntimeException e) {
                LOGGER.error("Can't insert client", e);
                sqlSession.rollback();
                checkDuplicate(e, OnlineShopErrorCode.USER_ALREADY_EXISTS);
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return client;
    }

    @Override
    public Client getById(int id) throws OnlineShopException {
        LOGGER.debug("DAO get client by Id = {}", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                Client client = getClientMapper(sqlSession).getById(id);
                if (client == null)
                    throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_EXISTS);
                return client;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get client");
                e.printStackTrace();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public List<Client> getAll() throws OnlineShopException {
        LOGGER.debug("DAO get all clients");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getClientMapper(sqlSession).getAll();
            } catch (RuntimeException e) {
                LOGGER.error("Can't get all clients");
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public void update(Client client) throws OnlineShopException {
        LOGGER.debug("DAO update client {}", client);
        try (SqlSession sqlSession = getSession()) {
            try {
                getClientMapper(sqlSession).update(client);
            } catch (RuntimeException e) {
                LOGGER.error("Can't update client");
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void updateDeposit(Client client) throws OnlineShopException {
        LOGGER.debug("DAO update client {} deposit", client);
        try (SqlSession sqlSession = getSession()) {
            try {
                int result = getClientMapper(sqlSession).updateDeposit(client);
                if (result == 0) {
                    sqlSession.rollback();
                    throw new OnlineShopException(OnlineShopErrorCode.DEPOSIT_WAS_CHANGED);
                }
            } catch (RuntimeException e) {
                LOGGER.error("Can't update client {} deposit", client);
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void delete(Client client) throws OnlineShopException {
        LOGGER.debug("DAO delete client {}", client);
        try (SqlSession sqlSession = getSession()) {
            try {
                getClientMapper(sqlSession).delete(client);
            } catch (RuntimeException e) {
                LOGGER.error("Can't delete client");
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void deleteAll() throws OnlineShopException {
        LOGGER.debug("DAO delete all clients");
        try (SqlSession sqlSession = getSession()) {
            try {
                getClientMapper(sqlSession).deleteAll();
            } catch (RuntimeException e) {
                LOGGER.error("Can't delete all clients");
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }
}