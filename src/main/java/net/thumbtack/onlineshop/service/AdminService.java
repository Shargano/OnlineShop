package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dao.AdminDao;
import net.thumbtack.onlineshop.dao.ClientDao;
import net.thumbtack.onlineshop.dao.SessionDao;
import net.thumbtack.onlineshop.dto.request.CreateAdminRequest;
import net.thumbtack.onlineshop.dto.request.UpdateAdminRequest;
import net.thumbtack.onlineshop.dto.response.LoginResponse;
import net.thumbtack.onlineshop.dto.response.account.AdminAccountInfoResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService extends ServiceBase {
    @Autowired
    public AdminService(SessionDao sessionDao, AdminDao adminDao, ClientDao clientDao) {
        super(sessionDao, adminDao, clientDao);
    }

    public LoginResponse create(CreateAdminRequest request) throws OnlineShopException {
        Administrator admin = adminDao.insert(new Administrator(request.getFirstName(), request.getLastName(), request.getPatronymic(), request.getPosition(), request.getLogin(), request.getPassword()));
        return login(admin);
    }

    public AdminAccountInfoResponse update(String sessionId, UpdateAdminRequest request) throws OnlineShopException {
        Administrator admin = getAdminFromDB(sessionId);
        if (!admin.getPassword().equals(request.getOldPassword()))
            throw new OnlineShopException(OnlineShopErrorCode.PASSWORD_FAILED);
        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setPatronymic(request.getPatronymic());
        admin.setPosition(request.getPosition());
        admin.setPassword(request.getNewPassword());
        adminDao.update(admin);
        return new AdminAccountInfoResponse(admin.getId(), admin.getFirstName(), admin.getLastName(), admin.getPatronymic(), admin.getPosition());
    }
}