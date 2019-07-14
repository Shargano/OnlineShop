package net.thumbtack.onlineshop.dto.response;

import java.util.Objects;

public class BasketItemResponse {
    private int id;
    private String name;
    private int price;
    private int count;

    public BasketItemResponse() {
    }

    public BasketItemResponse(int id, String name, int price, Integer count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasketItemResponse)) return false;
        BasketItemResponse that = (BasketItemResponse) o;
        return getId() == that.getId() &&
                getPrice() == that.getPrice() &&
                getCount() == that.getCount() &&
                Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPrice(), getCount());
    }
}