package net.thumbtack.onlineshop.model;

import java.util.Objects;

public class Purchase {
    private int id;
    private Client client;
    private Product product;
    private String productName;
    private int productPrice;
    private int count;

    public Purchase() {
    }

    public Purchase(Client client, Product product, String productName, int productPrice, int count) {
        this.client = client;
        this.product = product;
        this.productName = productName;
        this.productPrice = productPrice;
        this.count = count;
    }

    public Purchase(int id, Client client, Product product, int count) {
        this.id = id;
        this.client = client;
        this.product = product;
        this.productName = product.getName();
        this.productPrice = product.getPrice();
        this.count = count;
    }

    public Purchase(Client client, Product product, int count) {
        this(0, client, product, count);
    }

    public Purchase(Client client, Product product) {
        this(0, client, product, 1);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
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
        if (!(o instanceof Purchase)) return false;
        Purchase purchase = (Purchase) o;
        return getId() == purchase.getId() &&
                getProductPrice() == purchase.getProductPrice() &&
                getCount() == purchase.getCount() &&
                Objects.equals(getClient(), purchase.getClient()) &&
                Objects.equals(getProduct(), purchase.getProduct()) &&
                Objects.equals(getProductName(), purchase.getProductName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getClient(), getProduct(), getProductName(), getProductPrice(), getCount());
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", client=" + client +
                ", product=" + product +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", count=" + count +
                '}';
    }
}