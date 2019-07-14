package net.thumbtack.onlineshop.dto.request;

import net.thumbtack.onlineshop.validator.PasswordLength;
import net.thumbtack.onlineshop.validator.TextLength;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CreateAdminRequest {
    @NotBlank(message = "FirstName can't be empty")
    @NotNull(message = "FirstName can'be null")
    @TextLength(message = "FirstName is too long")
    @Pattern(regexp = "[а-яА-Я\\s\\-]+", message = "FirstName must contains only Russian letters and spaces")
    private String firstName;

    @NotBlank(message = "LastName can't be empty")
    @NotNull(message = "LastName can'be null")
    @TextLength(message = "LastName is too long")
    @Pattern(regexp = "[а-яА-Я\\s\\-]+", message = "LastName must contains only Russian letters and spaces")
    private String lastName;

    @TextLength(message = "Patronymic is too long")
    @Pattern(regexp = "[а-яА-Я\\s\\-]+", message = "Patronymic must contains only Russian letters and spaces")
    private String patronymic;

    @NotBlank(message = "Position can't be empty")
    @NotNull(message = "Position can'be null")
    private String position;

    @NotBlank(message = "Login can't be empty")
    @NotNull(message = "Login can'be null")
    @TextLength(message = "Login is too long")
    @Pattern(regexp = "[a-zA-Zа-яА-Я0-9]+", message = "Login must contains only Russian|English letters and digits")
    private String login;

    @NotBlank(message = "Password can't be empty")
    @NotNull(message = "Password can'be null")
    @TextLength(message = "Password is too long")
    @PasswordLength(message = "Password is too short")
    private String password;

    public CreateAdminRequest() {
    }

    public CreateAdminRequest(String firstName, String lastName, String patronymic, String position, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.position = position;
        this.login = login;
        this.password = password;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
