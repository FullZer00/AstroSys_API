package com.cp.danek.astroAPI.dto;

import com.cp.danek.astroAPI.model.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "DTO для представления пользователя системы")
public class UserDTO {

    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;

    @Schema(description = "Логин пользователя", example = "astronomer_ivanov")
    private String login;

    @Schema(description = "Полное имя пользователя", example = "Иванов Иван Иванович")
    private String fullName;

    @Schema(description = "Email пользователя", example = "ivanov@observatory.ru")
    private String email;

    @Schema(description = "Телефон пользователя", example = "+7-999-123-45-67")
    private String phone;

    @Schema(description = "Роль пользователя в системе")
    private UserRole role;

    @Schema(description = "Дата и время создания учетной записи")
    private LocalDateTime createdAt;

    @Schema(description = "Дата и время последнего входа")
    private LocalDateTime lastLogin;

    @Schema(description = "Статус активности учетной записи", example = "true")
    private Boolean isActive;

    // Конструкторы
    public UserDTO() {}

    public UserDTO(Long id, String login, String fullName, String email, String phone,
                   UserRole role, LocalDateTime createdAt, LocalDateTime lastLogin, Boolean isActive) {
        this.id = id;
        this.login = login;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
        this.isActive = isActive;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}