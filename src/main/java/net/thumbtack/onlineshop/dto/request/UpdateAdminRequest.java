package net.thumbtack.onlineshop.dto.request;

import net.thumbtack.onlineshop.validator.PasswordLength;
import net.thumbtack.onlineshop.validator.TextLength;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UpdateAdminRequest {
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

    public UpdateAdminRequest() {
    }

    public UpdateAdminRequest(String firstName, String lastName, String patronymic, String position, String oldPassword, String newPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.position = position;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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