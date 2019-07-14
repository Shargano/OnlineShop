package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.response.EmptyResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.service.CommonService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
public class DebugController {
    private final CommonService commonService;

    public DebugController(CommonService commonService) {
        this.commonService = commonService;
    }

    @PostMapping("/clear")
    public EmptyResponse clearDataBase() throws OnlineShopException {
        commonService.clear();
        return new EmptyResponse();
    }
}