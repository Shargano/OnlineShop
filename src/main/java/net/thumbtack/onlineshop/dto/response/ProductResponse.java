package net.thumbtack.onlineshop.dto.response;

import java.util.List;
import java.util.Objects;

public class ProductResponse {
    private int id;
    private String name;
    private int price;
    private int count;
    private List<Integer> categories;

    public ProductResponse() {
    }

    public ProductResponse(int id, String name, int price, int count, List<Integer> categories) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.categories = categories;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductResponse)) return false;
        ProductResponse response = (ProductResponse) o;
        return getId() == response.getId() &&
                getPrice() == response.getPrice() &&
                getCount() == response.getCount() &&
                Objects.equals(getName(), response.getName()) &&
                Objects.equals(getCategories(), response.getCategories());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPrice(), getCount(), getCategories());
    }
}