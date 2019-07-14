package net.thumbtack.onlineshop.dto.response.report;

import net.thumbtack.onlineshop.dto.response.PurchaseResponse;
import net.thumbtack.onlineshop.dto.response.account.ClientAccountInfoResponse;

public class CategoryReport {
    private Integer id;
    private String name;
    private String parentName;
    private ClientAccountInfoResponse client; // но без депозита
    private PurchaseResponse purchase;

    public CategoryReport() {
    }

    public CategoryReport(Integer id, String name, String parentName, ClientAccountInfoResponse client, PurchaseResponse purchase) {
        this.id = id;
        this.name = name;
        this.parentName = parentName;
        this.client = client;
        this.purchase = purchase;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public ClientAccountInfoResponse getClient() {
        return client;
    }

    public void setClient(ClientAccountInfoResponse client) {
        this.client = client;
    }

    public PurchaseResponse getPurchase() {
        return purchase;
    }

    public void setPurchase(PurchaseResponse purchase) {
        this.purchase = purchase;
    }
}