package net.thumbtack.onlineshop.daoimpl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.mappers.*;
import net.thumbtack.onlineshop.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

public class DaoImplBase {
    private static final int ER_DUP_ENTRY = 1062;
    private static final int ER_DUP_ENTRY_WITH_KEY_NAME = 1586;

    protected void checkDuplicate(RuntimeException e, OnlineShopErrorCode code) throws OnlineShopException {
        if (e.getCause() instanceof MySQLIntegrityConstraintViolationException) {
            MySQLIntegrityConstraintViolationException mySQLEx = (MySQLIntegrityConstraintViolationException) e.getCause();
            switch (mySQLEx.getErrorCode()) {
                case ER_DUP_ENTRY:
                case ER_DUP_ENTRY_WITH_KEY_NAME:
                    throw new OnlineShopException(code);
            }
        }
    }

    protected SqlSession getSession() {
        return MyBatisUtils.getSqlSessionFactory().openSession();
    }

    protected UserMapper getUserMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(UserMapper.class);
    }

    protected AdminMapper getAdminMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(AdminMapper.class);
    }

    protected ClientMapper getClientMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(ClientMapper.class);
    }

    protected CategoryMapper getCategoryMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(CategoryMapper.class);
    }

    protected ProductMapper getProductMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(ProductMapper.class);
    }

    protected BasketMapper getBasketMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(BasketMapper.class);
    }

    protected BasketProductItemMapper getBasketProductItemMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(BasketProductItemMapper.class);
    }

    protected PurchaseMapper getPurchaseMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(PurchaseMapper.class);
    }

    protected SessionMapper getSessionMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(SessionMapper.class);
    }
}