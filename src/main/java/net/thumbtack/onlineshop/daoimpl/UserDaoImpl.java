package net.thumbtack.onlineshop.daoimpl;

import net.thumbtack.onlineshop.dao.UserDao;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.User;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserDaoImpl extends DaoImplBase implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public User getByLogin(String login) throws OnlineShopException {
        LOGGER.debug("DAO get user by login = {}", login);
        try (SqlSession sqlSession = getSession()) {
            try {
                User user = getUserMapper(sqlSession).getByLogin(login);
                if (user == null)
                    throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_EXISTS);
                return user;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get user");
                e.printStackTrace();
                throw new OnlineShopException(OnlineShopErrorCode.DATABASE_ERROR);
            }
        }
    }
}