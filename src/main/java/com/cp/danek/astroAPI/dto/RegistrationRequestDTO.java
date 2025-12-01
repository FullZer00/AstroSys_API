package com.cp.danek.astroAPI.dto;

import com.cp.danek.astroAPI.model.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Запрос на регистрацию")
public class RegistrationRequestDTO {

    @NotBlank(message = "Логин не может быть пустым")
    @Schema(description = "Логин пользователя", example = "astronomer_ivanov")
    private String login;

    @NotBlank(message = "Пароль не может быть пустым")
    @Schema(description = "Пароль пользователя", example = "Password123")
    private String password;

    @Schema(description = "Проверочный пароль")
    private String repeatPassword;

    @NotBlank(message = "ФИО не может быть пустым")
    @Schema(description = "Эл. почта пользователя", example = "ivanov@gmail.com")
    private String email;

    @NotBlank(message = "ФИО не может быть пустым")
    @Schema(description = "ФИО пользователя", example = "Иванов Иван Иванович")
    private String fullName;

    @Schema(description = "Роль пользователя", example = "ASTRONOMER")
    private UserRole role;

    @AssertTrue(message = "Пароли не совпадают")
    public boolean isPasswordsMatch() {
        return password != null && password.equals(repeatPassword);
    }

    public RegistrationRequestDTO() {}

    // Конструктор
    public RegistrationRequestDTO(String login, String password, String repeatPassword, String email, String fullName, UserRole role) {
        this.login = login;
        this.password = password;
        this.repeatPassword = repeatPassword;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }

    public @NotBlank(message = "Логин не может быть пустым") String getLogin() {
        return login;
    }

    public void setLogin(@NotBlank(message = "Логин не может быть пустым") String login) {
        this.login = login;
    }

    public @NotBlank(message = "Пароль не может быть пустым") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Пароль не может быть пустым") String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public @NotBlank(message = "ФИО не может быть пустым") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "ФИО не может быть пустым") String email) {
        this.email = email;
    }

    public @NotBlank(message = "ФИО не может быть пустым") String getFullName() {
        return fullName;
    }

    public void setFullName(@NotBlank(message = "ФИО не может быть пустым") String fullName) {
        this.fullName = fullName;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
