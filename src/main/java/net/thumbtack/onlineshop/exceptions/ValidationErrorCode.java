package net.thumbtack.onlineshop.exceptions;

public enum ValidationErrorCode {
    ADDRESS("address", "Invalid 'address' field!"),
    COUNT("count", "Invalid 'count' field!"),
    DEPOSIT("deposit", "Invalid 'deposit' field!"),
    EMAIL("email", "Invalid 'email' field!"),
    FIRSTNAME("firstName", "Invalid 'firstName' field!"),
    LASTNAME("lastName", "Invalid 'lastName' field!"),
    LOGIN("login", "Invalid 'login' field!"),
    NAME("name", "Invalid 'name' field!"),
    NEWPASSWORD("newPassword", "Invalid 'newPassword' field!"),
    OLDPASSWORD("oldPassword", "Invalid 'oldPassword' field!"),
    PASSWORD("password", "Invalid 'password' field!"),
    PATRONYMIC("patronymic", "Invalid 'patronymic' field!"),
    PHONE("phone", "Invalid 'phone' field!"),
    POSITION("position", "Invalid 'position' field!"),
    PRICE("price", "Invalid 'price' field!");

    private String field;
    private String message;

    ValidationErrorCode(String field, String message) {
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
