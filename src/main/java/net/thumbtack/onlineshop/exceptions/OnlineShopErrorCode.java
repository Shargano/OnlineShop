package net.thumbtack.onlineshop.exceptions;

public enum OnlineShopErrorCode {
    BASKET_NOT_EXISTS("id", "The client's basket doesn't exists (may be client doesn't exists)"),
    BODY("body", "Body of request is wrong!"),
    CATEGORY_ALREADY_EXISTS("name", "Category with the same name already exists"),
    CATEGORY_FIELDS("name|parentId", "One of these fields must be not null!"),
    CATEGORY_NOT_EXISTS("id", "Category with this id doesn't exists"),
    CATEGORY_PARENT("parentId", "Can't change category parent"),
    DATABASE_ERROR("database", "Some error in database!"),
    DEPOSIT_WAS_CHANGED("version", "Deposit with this id was changed!"),
    NOT_ENOUGH_MONEY("deposit", "You don't have enough money to make this purchase"),
    NOT_ENOUGH_PRODUCT_COUNT("count", "We don't have so many units of product"),
    NOT_FOUND("body", "The page is not found!"),
    PASSWORD_FAILED("password", "The user has different password"),
    PRODUCT_NAME_INCORRECT("name", "Product with this id has different name"),
    PRODUCT_NOT_EXISTS("id", "Product with this id doesn't exists"),
    PRODUCT_NOT_IN_BASKET("id", "You don't have this product in your basket"),
    PRODUCT_PRICE_INCORRECT("price", "Product with this id has different price"),
    PRODUCT_WAS_CHANGED("version", "Product with this id was changed"),
    SERVER("server", "Something was wrong... \nWrite to us about this error"),
    SESSION_NOT_EXIST("sessionId", "Session with this user doesn't exists!"),
    UNKNOWN_USER_TYPE("userType", "Unknown user type hab been used"),
    USER_ALREADY_EXISTS("login", "User with the same login is already exists"),
    USER_NOT_ADMIN("userType", "User doesn't have administrator rights"),
    USER_NOT_CLIENT("userType", "User doesn't have client rights"),
    USER_NOT_EXISTS("login", "Client with this login doesn't exist"),
    WRONG_ORDER("order", "Can't order results by this field");

    private String field;
    private String message;

    OnlineShopErrorCode(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}