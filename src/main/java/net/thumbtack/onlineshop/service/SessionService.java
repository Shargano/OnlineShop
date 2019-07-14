package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dao.AdminDao;
import net.thumbtack.onlineshop.dao.ClientDao;
import net.thumbtack.onlineshop.dao.SessionDao;
import net.thumbtack.onlineshop.dao.UserDao;
import net.thumbtack.onlineshop.dto.request.LoginRequest;
import net.thumbtack.onlineshop.dto.response.EmptyResponse;
import net.thumbtack.onlineshop.dto.response.LoginResponse;
import net.thumbtack.onlineshop.dto.response.account.AccountInfoResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Session;
import net.thumbtack.onlineshop.model.User;
import org.springframework.stereotype.Service;

@Service
public class SessionService extends ServiceBase {
    private final UserDao userDao;

    public SessionService(SessionDao sessionDao, AdminDao adminDao, ClientDao clientDao, UserDao userDao) {
        super(sessionDao, adminDao, clientDao);
        this.userDao = userDao;
    }

    public LoginResponse create(LoginRequest request) throws OnlineShopException {
        User user = userDao.getByLogin(request.getLogin());
        if (!user.getPassword().equals(request.getPassword()))
            throw new OnlineShopException(OnlineShopErrorCode.PASSWORD_FAILED);
        return login(user);
    }

    public EmptyResponse delete(String sessionId) throws OnlineShopException {
        Session session = sessionDao.getById(sessionId);
        sessionDao.delete(session);
        return new EmptyResponse();
    }

    public AccountInfoResponse getAccountInfo(String sessionId) throws OnlineShopException {
        return super.getAccountInfo(sessionId);
    }
}