package com.cp.danek.astroAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class JwtResponseDTO {
    @Schema(description = "JWT токен", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Тип токена", example = "Bearer")
    private String type = "Bearer";

    @Schema(description = "ID пользователя", example = "1")
    private Long id;

    @Schema(description = "Логин пользователя", example = "astronomer_ivanov")
    private String login;

    @Schema(description = "Роль пользователя", example = "ASTRONOMER")
    private String role;

    // Конструкторы
    public JwtResponseDTO(String token, Long id, String login, String role) {
        this.token = token;
        this.id = id;
        this.login = login;
        this.role = role;
    }

    // Геттеры и сеттеры
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
