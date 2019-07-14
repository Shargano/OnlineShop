package net.thumbtack.onlineshop.exceptions;

public class OnlineShopException extends Exception {
    private OnlineShopErrorCode onlineShopErrorCode;

    public OnlineShopException(OnlineShopErrorCode onlineShopErrorCode) {
        this.onlineShopErrorCode = onlineShopErrorCode;
    }

    public OnlineShopErrorCode getErrorCode() {
        return onlineShopErrorCode;
    }
}