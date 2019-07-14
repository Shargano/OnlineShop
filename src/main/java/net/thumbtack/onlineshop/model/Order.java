package net.thumbtack.onlineshop.model;

public enum Order {
    PRODUCT("product"), CATEGORY("category");

    private String field;

    Order(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
