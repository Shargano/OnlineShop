package net.thumbtack.onlineshop.daoimpl;

import net.thumbtack.onlineshop.dao.CommonDao;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CommonDaoImpl extends DaoImplBase implements CommonDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonDaoImpl.class);

    @Override
    public void clear() throws OnlineShopException {
        LOGGER.debug("DAO clear database");
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).deleteAll();
                getProductMapper(sqlSession).deleteAll();
                getCategoryMapper(sqlSession).deleteAll();
            } catch (RuntimeException e) {
                LOGGER.error("Can't clear database");
                sqlSession.rollback();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }
}