package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.response.SettingResponse;
import net.thumbtack.onlineshop.service.CommonService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
public class SettingController {
    private final CommonService commonService;

    public SettingController(CommonService commonService) {
        this.commonService = commonService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public SettingResponse getSettings(@CookieValue("JAVASESSIONID") String sessionId) {
        return commonService.getSettings();
    }
}