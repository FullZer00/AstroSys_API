package com.cp.danek.astroAPI.model.enums;

public enum TelescopeStatus {
    AVAILABLE("Доступен"),
    MAINTENANCE("На обслуживании"),
    IN_USE("В использовании"),
    OFFLINE("Неактивен");

    private final String displayName;

    TelescopeStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}