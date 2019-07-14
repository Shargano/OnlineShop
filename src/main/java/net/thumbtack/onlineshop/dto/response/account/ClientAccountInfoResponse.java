package net.thumbtack.onlineshop.dto.response.account;

import net.thumbtack.onlineshop.model.Deposit;

public class ClientAccountInfoResponse extends AccountInfoResponse {
    private String email;
    private String address;
    private String phone;
    private Deposit deposit;

    public ClientAccountInfoResponse() {
    }

    public ClientAccountInfoResponse(int id, String firstName, String lastName, String patronymic, String email, String address, String phone, Deposit deposit) {
        super(id, firstName, lastName, patronymic);
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.deposit = deposit;
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

    public Deposit getDeposit() {
        return deposit;
    }

    public void setDeposit(Deposit deposit) {
        this.deposit = deposit;
    }
}
