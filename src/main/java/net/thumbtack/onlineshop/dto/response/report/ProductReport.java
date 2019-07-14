package net.thumbtack.onlineshop.dto.response.report;

import net.thumbtack.onlineshop.dto.response.account.ClientAccountInfoResponse;

public class ProductReport {
    private int purchaseId;
    private Integer productId;
    private String productName;
    private int productPrice;
    private int count;
    private ClientAccountInfoResponse client; // но без депозита

    public ProductReport() {
    }

    public ProductReport(int purchaseId, Integer productId, String productName, int productPrice, int count, ClientAccountInfoResponse client) {
        this.purchaseId = purchaseId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.count = count;
        this.client = client;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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

    public ClientAccountInfoResponse getClient() {
        return client;
    }

    public void setClient(ClientAccountInfoResponse client) {
        this.client = client;
    }
}