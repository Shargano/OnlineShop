package net.thumbtack.onlineshop.daoimpl;

import net.thumbtack.onlineshop.dao.AdminDao;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Administrator;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AdminDaoImpl extends DaoImplBase implements AdminDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Override
    public Administrator insert(Administrator admin) throws OnlineShopException {
        LOGGER.debug("DAO insert admin {}", admin);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insert(admin);
                getAdminMapper(sqlSession).insert(admin);
            } catch (RuntimeException e) {
                LOGGER.error("Can't insert admin", e);
                sqlSession.rollback();
                checkDuplicate(e, OnlineShopErrorCode.USER_ALREADY_EXISTS);
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return admin;
    }

    @Override
    public Administrator getById(int id) throws OnlineShopException {
        LOGGER.debug("DAO get admin by Id = {}", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                Administrator admin = getAdminMapper(sqlSession).getById(id);
                if (admin == null)
                    throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_EXISTS);
                return admin;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get admin");
                e.printStackTrace();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public void update(Administrator admin) throws OnlineShopException {
        LOGGER.debug("DAO update admin {}", admin);
        try (SqlSession sqlSession = getSession()) {
            try {
                getAdminMapper(sqlSession).update(admin);
            } catch (RuntimeException e) {
                LOGGER.error("Can't update admin");
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void delete(Administrator admin) throws OnlineShopException {
        LOGGER.debug("DAO delete admin {}", admin);
        try (SqlSession sqlSession = getSession()) {
            try {
                getAdminMapper(sqlSession).delete(admin);
            } catch (RuntimeException e) {
                LOGGER.error("Can't delete admin");
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }
}
