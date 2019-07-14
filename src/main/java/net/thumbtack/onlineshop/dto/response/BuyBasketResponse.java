package net.thumbtack.onlineshop.dto.response;

import java.util.List;

public class BuyBasketResponse {
    List<PurchaseResponse> bought;
    List<PurchaseResponse> remaining;

    public BuyBasketResponse() {
    }

    public BuyBasketResponse(List<PurchaseResponse> bought, List<PurchaseResponse> remaining) {
        this.bought = bought;
        this.remaining = remaining;
    }

    public List<PurchaseResponse> getBought() {
        return bought;
    }

    public void setBought(List<PurchaseResponse> bought) {
        this.bought = bought;
    }

    public List<PurchaseResponse> getRemaining() {
        return remaining;
    }

    public void setRemaining(List<PurchaseResponse> remaining) {
        this.remaining = remaining;
    }
}
