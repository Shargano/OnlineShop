package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dao.AdminDao;
import net.thumbtack.onlineshop.dao.ClientDao;
import net.thumbtack.onlineshop.dao.SessionDao;
import net.thumbtack.onlineshop.dto.response.LoginResponse;
import net.thumbtack.onlineshop.dto.response.account.AccountInfoResponse;
import net.thumbtack.onlineshop.dto.response.account.AdminAccountInfoResponse;
import net.thumbtack.onlineshop.dto.response.account.ClientAccountInfoResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ServiceBase {
    protected final SessionDao sessionDao;
    protected final AdminDao adminDao;
    protected final ClientDao clientDao;

    @Autowired
    public ServiceBase(SessionDao sessionDao, AdminDao adminDao, ClientDao clientDao) {
        this.sessionDao = sessionDao;
        this.adminDao = adminDao;
        this.clientDao = clientDao;
    }

    protected LoginResponse login(User user) throws OnlineShopException {
        Session session = sessionDao.getByUserId(user.getId());
        if (session != null) {
            sessionDao.delete(session);
        }
        session = sessionDao.insert(new Session(UUID.randomUUID().toString(), user));
        AccountInfoResponse infoResponse = getAccountInfo(session.getId());
        return new LoginResponse(session.getId(), infoResponse);
    }

    protected AccountInfoResponse getAccountInfo(String sessionId) throws OnlineShopException {
        User user = getUserFromDB(sessionId);
        switch (user.getUserType()) {
            case ADMIN:
                Administrator admin = adminDao.getById(user.getId());
                return new AdminAccountInfoResponse(admin.getId(), admin.getFirstName(), admin.getLastName(), admin.getPatronymic(), admin.getPosition());
            case CLIENT:
                Client client = clientDao.getById(user.getId());
                return new ClientAccountInfoResponse(client.getId(), client.getFirstName(), client.getLastName(), client.getPatronymic(), client.getEmail(), client.getAddress(), client.getPhone(), client.getDeposit());
            default:
                throw new OnlineShopException(OnlineShopErrorCode.UNKNOWN_USER_TYPE);
        }
    }

    protected User getUserFromDB(String sessionId) throws OnlineShopException {
        Session session = sessionDao.getById(sessionId);
        return session.getUser();
    }

    protected Administrator getAdminFromDB(String sessionId) throws OnlineShopException {
        User user = getUserFromDB(sessionId);
        checkUserIsAdmin(user);
        return adminDao.getById(user.getId());
    }

    protected Client getClientFromDB(String sessionId) throws OnlineShopException {
        User user = getUserFromDB(sessionId);
        checkUserIsClient(user);
        return clientDao.getById(user.getId());
    }

    protected void checkUserIsAdmin(User user) throws OnlineShopException {
        if (user.getUserType() != UserType.ADMIN)
            throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_ADMIN);
    }

    protected void checkUserIsClient(User user) throws OnlineShopException {
        if (user.getUserType() != UserType.CLIENT)
            throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_CLIENT);

    }
}