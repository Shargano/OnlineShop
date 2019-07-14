package net.thumbtack.onlineshop.model;

import java.util.Objects;

public class BasketProductItem {
    private int id;
    private Product product;
    private int count;

    private BasketProductItem() {
    }

    public BasketProductItem(int id, Product product, int count) {
        this.id = id;
        this.product = product;
        this.count = count;
    }

    public BasketProductItem(Product product, int count) {
        this(0, product, count);
    }

    public BasketProductItem(Product product) {
        this(0, product, 1);
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasketProductItem)) return false;
        BasketProductItem item = (BasketProductItem) o;
        return getId() == item.getId() &&
                getCount() == item.getCount() &&
                Objects.equals(getProduct(), item.getProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getProduct(), getCount());
    }

    @Override
    public String toString() {
        return "BasketProductItem{" +
                "id=" + id +
                ", product=" + product +
                ", count=" + count +
                '}';
    }
}