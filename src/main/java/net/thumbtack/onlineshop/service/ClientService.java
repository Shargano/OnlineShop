package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dao.AdminDao;
import net.thumbtack.onlineshop.dao.ClientDao;
import net.thumbtack.onlineshop.dao.SessionDao;
import net.thumbtack.onlineshop.dto.request.CreateClientRequest;
import net.thumbtack.onlineshop.dto.request.DepositRequest;
import net.thumbtack.onlineshop.dto.request.UpdateClientRequest;
import net.thumbtack.onlineshop.dto.response.AllClientsInfoResponse;
import net.thumbtack.onlineshop.dto.response.LoginResponse;
import net.thumbtack.onlineshop.dto.response.account.ClientAccountInfoResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopErrorCode;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Deposit;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService extends ServiceBase {

    public ClientService(SessionDao sessionDao, AdminDao adminDao, ClientDao clientDao) {
        super(sessionDao, adminDao, clientDao);
    }

    public LoginResponse create(CreateClientRequest request) throws OnlineShopException {
        Client client = clientDao.insert(new Client(request.getFirstName(), request.getLastName(), request.getPatronymic(), request.getEmail(), request.getAddress(),
                request.getPhone().replaceAll("-", ""), request.getLogin(), request.getPassword()));
        return login(client);
    }

    public List<AllClientsInfoResponse> getAll(String sessionId) throws OnlineShopException {
        checkUserIsAdmin(getUserFromDB(sessionId));
        List<AllClientsInfoResponse> responses = new ArrayList<>();
        List<Client> clients = clientDao.getAll();
        clients.forEach(client -> responses.add(new AllClientsInfoResponse(client.getId(), client.getFirstName(), client.getLastName(), client.getPatronymic(), client.getEmail(), client.getAddress(), client.getPhone(), client.getUserType().toString())));
        return responses;
    }

    public ClientAccountInfoResponse update(String sessionId, UpdateClientRequest request) throws OnlineShopException {
        Client client = getClientFromDB(sessionId);
        if (!client.getPassword().equals(request.getOldPassword()))
            throw new OnlineShopException(OnlineShopErrorCode.PASSWORD_FAILED);
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setEmail(request.getEmail());
        client.setAddress(request.getAddress());
        client.setPatronymic(request.getPatronymic());
        client.setPhone(request.getPhone());
        client.setPassword(request.getNewPassword());
        clientDao.update(client);
        return new ClientAccountInfoResponse(client.getId(), client.getFirstName(), client.getLastName(), client.getPatronymic(), client.getEmail(), client.getAddress(), client.getPhone(), new Deposit(0));
    }

    public ClientAccountInfoResponse addMoney(String sessionId, DepositRequest request) throws OnlineShopException {
        Client client = getClientFromDB(sessionId);
        client.addToDeposit(request.getDeposit());
        clientDao.updateDeposit(client);
        return new ClientAccountInfoResponse(client.getId(), client.getFirstName(), client.getLastName(), client.getPatronymic(), client.getEmail(), client.getAddress(), client.getPhone(), client.getDeposit());
    }

    public ClientAccountInfoResponse getDeposit(String sessionId) throws OnlineShopException {
        Client client = getClientFromDB(sessionId);
        return new ClientAccountInfoResponse(client.getId(), client.getFirstName(), client.getLastName(), client.getPatronymic(), client.getEmail(), client.getAddress(), client.getPhone(), client.getDeposit());
    }
}