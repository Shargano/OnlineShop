package net.thumbtack.onlineshop.dto.response.report;

import net.thumbtack.onlineshop.dto.response.PurchaseResponse;

import java.util.List;

public class ClientReport {
    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;
    private String address;
    private String phone;
    private int costOfPurchases;
    private List<PurchaseResponse> purchases;

    public ClientReport() {
    }

    public ClientReport(int id, String firstName, String lastName, String patronymic, String email, String address, String phone, int costOfPurchases, List<PurchaseResponse> purchases) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.costOfPurchases = costOfPurchases;
        this.purchases = purchases;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCostOfPurchases() {
        return costOfPurchases;
    }

    public void setCostOfPurchases(int costOfPurchases) {
        this.costOfPurchases = costOfPurchases;
    }

    public List<PurchaseResponse> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<PurchaseResponse> purchases) {
        this.purchases = purchases;
    }
}