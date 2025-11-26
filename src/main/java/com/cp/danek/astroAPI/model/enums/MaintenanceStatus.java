package com.cp.danek.astroAPI.model.enums;

public enum MaintenanceStatus {
    SCHEDULED("Запланировано"),
    IN_PROGRESS("В процессе"),
    COMPLETED("Завершено"),
    CANCELLED("Отменено");

    private final String displayName;

    MaintenanceStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}