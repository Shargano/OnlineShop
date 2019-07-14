package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dao.CommonDao;
import net.thumbtack.onlineshop.dto.response.SettingResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommonService {
    private final CommonDao commonDao;
    @Value("${server.port}")
    private int rest_http_port;
    @Value("${max_name_length}")
    private int maxNameLength;
    @Value("${min_password_length}")
    private int minPasswordLength;

    @Autowired
    public CommonService(CommonDao commonDao) {
        this.commonDao = commonDao;
    }

    public void clear() throws OnlineShopException {
        commonDao.clear();
    }

    public SettingResponse getSettings() {
        return new SettingResponse(rest_http_port, maxNameLength, minPasswordLength);
    }
}