package com.cp.danek.astroAPI.model.enums;

public enum UserRole {
    ASTRONOMER("Астроном"),
    ENGINEER("Инженер"),
    ADMIN("Администратор"),
    STUDENT("Студент"),
    GUEST("Гость");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}