package com.cp.danek.astroAPI.dto;

import com.cp.danek.astroAPI.model.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на создание нового пользователя")
public class CreateUserDTO {

    @Schema(description = "Логин пользователя", example = "new_astronomer")
    private String login;

    @Schema(description = "Пароль пользователя", example = "securePassword123")
    private String password;

    @Schema(description = "Полное имя пользователя", example = "Петров Петр Петрович")
    private String fullName;

    @Schema(description = "Email пользователя", example = "petrov@observatory.ru")
    private String email;

    @Schema(description = "Телефон пользователя", example = "+7-999-765-43-21")
    private String phone;

    @Schema(description = "Роль пользователя", example = "ASTRONOMER")
    private UserRole role;

    public CreateUserDTO() {
    }

    public CreateUserDTO(String login, String password, String fullName, String email, String phone, UserRole role) {
        this.login = login;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
