package net.thumbtack.onlineshop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Basket {
    private int id;
    private List<BasketProductItem> items = new ArrayList<>();

    public Basket() {
    }

    public Basket(int id, List<BasketProductItem> items) {
        this.id = id;
        this.items = items;
    }

    public void removeItem(BasketProductItem item) {
        items.remove(item);
    }

    public void addItem(BasketProductItem item) {
        items.add(item);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<BasketProductItem> getItems() {
        return items;
    }

    public void setItems(List<BasketProductItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Basket)) return false;
        Basket basket = (Basket) o;
        return getId() == basket.getId() &&
                Objects.equals(getItems(), basket.getItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getItems());
    }

    @Override
    public String toString() {
        return "Basket{" +
                "id=" + id +
                ", items=" + items +
                '}';
    }
}