package com.cp.danek.astroAPI.dto;

import com.cp.danek.astroAPI.model.enums.UserRole;

public class CreateUserDTO {
    private String login;
    private String password;
    private String fullName;
    private String email;
    private String phone;
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
