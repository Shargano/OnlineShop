package net.thumbtack.onlineshop.dto.request;

import net.thumbtack.onlineshop.validator.PasswordLength;
import net.thumbtack.onlineshop.validator.TextLength;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UpdateClientRequest {
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

    @NotBlank(message = "Email can't be empty")
    @NotNull(message = "Email can'be null")
    @Email(message = "Incorrect email format")
    @TextLength(message = "Email is too long")
    private String email;

    @NotBlank(message = "Address can't be empty")
    @NotNull(message = "Address can'be null")
    @TextLength(message = "Address is too long")
    private String address;

    @NotBlank(message = "Phone can't be empty")
    @NotNull(message = "Phone can'be null")
    @Pattern(regexp = "(\\+7|8)[-]?\\d{3}[-]?\\d{3}[-]?(\\d){2}[-]?(\\d){2}", message = "Incorrect phone format")
    private String phone;

    @NotBlank(message = "Password can't be empty")
    @NotNull(message = "Password can'be null")
    @TextLength(message = "Password is too long")
    @PasswordLength(message = "Password is too short")
    private String oldPassword;

    @NotBlank(message = "Password can't be empty")
    @NotNull(message = "Password can'be null")
    @TextLength(message = "Password is too long")
    @PasswordLength(message = "Password is too short")
    private String newPassword;

    public UpdateClientRequest() {
    }

    public UpdateClientRequest(String firstName, String lastName, String patronymic, String email, String address, String phone, String oldPassword, String newPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}