// LoginRequestDTO.java
package com.cp.danek.astroAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на аутентификацию")
public class LoginRequestDTO {

    @NotBlank(message = "Логин не может быть пустым")
    @Schema(description = "Логин пользователя", example = "astronomer_ivanov")
    private String login;

    @NotBlank(message = "Пароль не может быть пустым")
    @Schema(description = "Пароль пользователя", example = "password123")
    private String password;

    // Конструкторы, геттеры и сеттеры
    public LoginRequestDTO() {}

    public LoginRequestDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
