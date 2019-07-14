package net.thumbtack.onlineshop.daoimpl;

import net.thumbtack.onlineshop.dao.SessionDao;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Session;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SessionDaoImpl extends DaoImplBase implements SessionDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionDaoImpl.class);

    @Override
    public Session insert(Session session) throws OnlineShopException {
        LOGGER.debug("DAO create session {}", session);
        try (SqlSession sqlSession = getSession()) {
            try {
                getSessionMapper(sqlSession).insert(session);
            } catch (RuntimeException e) {
                LOGGER.error("Can't create session", e);
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return session;
    }

    @Override
    public Session getById(String id) throws OnlineShopException {
        LOGGER.debug("DAO get session by Id = {}", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                Session session = getSessionMapper(sqlSession).getById(id);
                if (session == null)
                    throw new OnlineShopException(OnlineShopErrorCode.SESSION_NOT_EXIST);
                return session;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get session by id = {}", id);
                e.printStackTrace();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public Session getByUserId(int userId) throws OnlineShopException {
        LOGGER.debug("DAO get session by user id = {}", userId);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getSessionMapper(sqlSession).getByUser(userId);
            } catch (RuntimeException e) {
                LOGGER.error("Can't get session by userId = {}", userId);
                e.printStackTrace();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public void delete(Session session) throws OnlineShopException {
        LOGGER.debug("DAO delete session {}", session);
        try (SqlSession sqlSession = getSession()) {
            try {
                getSessionMapper(sqlSession).delete(session);
            } catch (RuntimeException e) {
                LOGGER.error("Can't delete session");
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }
}
