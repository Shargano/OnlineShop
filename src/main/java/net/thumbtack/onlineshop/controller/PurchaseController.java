package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.request.PurchaseProductRequest;
import net.thumbtack.onlineshop.dto.response.BuyBasketResponse;
import net.thumbtack.onlineshop.dto.response.PurchaseResponse;
import net.thumbtack.onlineshop.dto.response.report.ReportResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PurchaseResponse buyProduct(@CookieValue("JAVASESSIONID") String sessionId, @Valid @RequestBody PurchaseProductRequest request) throws OnlineShopException {
        return purchaseService.buyProduct(sessionId, request);
    }

    @PostMapping(value = "/baskets", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BuyBasketResponse buyBasket(@CookieValue("JAVASESSIONID") String sessionId,
                                       @Valid @RequestBody List<PurchaseProductRequest> request) throws OnlineShopException {
        return purchaseService.buyBasket(sessionId, request);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ReportResponse getReport(@CookieValue("JAVASESSIONID") String sessionId,
                                    @RequestParam(name = "client", required = false) List<Integer> clients,
                                    @RequestParam(name = "product", required = false) List<Integer> products,
                                    @RequestParam(name = "category", required = false) List<Integer> categories,
                                    @RequestParam(name = "offset", required = false) Integer offset,
                                    @RequestParam(name = "limit", required = false) Integer limit,
                                    @RequestParam(name = "order", required = false) String order,
                                    @RequestParam(name = "onlyTotal", required = false, defaultValue = "false") Boolean onlyTotal) throws OnlineShopException {
        return purchaseService.getReport(sessionId, clients, products, categories, offset, limit, order, onlyTotal);
    }
}