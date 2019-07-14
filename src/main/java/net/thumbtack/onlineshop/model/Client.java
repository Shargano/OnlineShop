package net.thumbtack.onlineshop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Client extends User {
    private String email;
    private String address;
    private String phone;
    private Deposit deposit = new Deposit();
    private Basket basket = new Basket();
    private List<Purchase> purchases = new ArrayList<>();

    public Client() {
    }

    private Client(int id, String firstName, String lastName, String patronymic, String email, String address, String phone, Integer deposit, String login, String password) {
        super(id, firstName, lastName, patronymic, login, password, UserType.CLIENT);
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.deposit.setValue(deposit);
    }

    public Client(String firstName, String lastName, String patronymic, String email, String address, String phone, String login, String password) {
        this(0, firstName, lastName, patronymic, email, address, phone, 0, login, password);
    }

    public Client(String firstName, String lastName, String email, String address, String phone, String login, String password) {
        this(0, firstName, lastName, null, email, address, phone, 0, login, password);
    }

    public void reduceDeposit(int value) {
        getDeposit().reduce(value);
    }

    public void addToDeposit(int value) {
        getDeposit().add(value);
    }

    public void adjustProductInBasket(Product product, Integer count) {
        for (BasketProductItem item : getBasket().getItems()) {
            if (item.getProduct().getId() == product.getId()) {
                item.reduceCount(count);
                if (item.getCount() == 0) {
                    getBasket().removeItem(item);
                    break;
                }
            }
        }
    }

    public Product getProductFromBasket(int productId) {
        for (BasketProductItem item : getBasket().getItems()) {
            if (item.getProduct().getId() == productId)
                return item.getProduct();
        }
        return null;
    }

    public BasketProductItem getItemFromBasket(int productId) {
        for (BasketProductItem item : getBasket().getItems()) {
            if (item.getProduct().getId() == productId)
                return item;
        }
        return null;
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

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        if (!super.equals(o)) return false;
        Client client = (Client) o;
        return Objects.equals(getEmail(), client.getEmail()) &&
                Objects.equals(getAddress(), client.getAddress()) &&
                Objects.equals(getPhone(), client.getPhone()) &&
                Objects.equals(getDeposit(), client.getDeposit()) &&
                Objects.equals(getBasket(), client.getBasket()) &&
                Objects.equals(purchases, client.purchases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getEmail(), getAddress(), getPhone(), getDeposit(), getBasket(), purchases);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", patronymic='" + getPatronymic() + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", login='" + getLogin() + '\'' +
                ", userType=" + getUserType() +
                '}';
    }
}