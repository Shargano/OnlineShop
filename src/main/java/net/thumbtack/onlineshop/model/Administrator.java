package net.thumbtack.onlineshop.model;

import java.util.Objects;

public class Administrator extends User {
    private String position;

    public Administrator() {
    }

    public Administrator(int id, String firstName, String lastName, String patronymic, String position, String login, String password) {
        super(id, firstName, lastName, patronymic, login, password, UserType.ADMIN);
        this.position = position;
    }

    public Administrator(String firstName, String lastName, String patronymic, String position, String login, String password) {
        this(0, firstName, lastName, patronymic, position, login, password);
    }

    public Administrator(String firstName, String lastName, String position, String login, String password) {
        this(0, firstName, lastName, null, position, login, password);
    }


    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Administrator)) return false;
        if (!super.equals(o)) return false;
        Administrator that = (Administrator) o;
        return Objects.equals(getPosition(), that.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPosition());
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", patronymic='" + getPatronymic() + '\'' +
                "position='" + position + '\'' +
                ", login='" + getLogin() + '\'' +
                ", userType=" + getUserType() +
                '}';
    }
}