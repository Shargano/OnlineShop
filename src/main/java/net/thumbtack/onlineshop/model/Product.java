package net.thumbtack.onlineshop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Product {
    private int id;
    private String name;
    private int price;
    private int count;
    private int version;
    private List<Category> categories;

    public Product() {
    }

    public Product(int id, String name, int price, int count, int version, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.version = version;
        this.categories = categories;
    }

    public Product(String name, int price, int count, List<Category> categories) {
        this(0, name, price, count, 1, categories);
    }

    public Product(String name, int price, int count) {
        this(0, name, price, count, 1, new ArrayList<>());
    }

    public Product(String name, int price, List<Category> categories) {
        this(0, name, price, 0, 1, categories);
    }

    public Product(String name, int price) {
        this(0, name, price, 0, 1, new ArrayList<>());
    }

    public void reduceCount(int count) {
        this.count -= count;
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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return getId() == product.getId() &&
                getPrice() == product.getPrice() &&
                getCount() == product.getCount() &&
                getVersion() == product.getVersion() &&
                Objects.equals(getName(), product.getName()) &&
                Objects.equals(getCategories(), product.getCategories());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPrice(), getCount(), getVersion(), getCategories());
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", version=" + version +
                ", categories=" + categories +
                '}';
    }
}