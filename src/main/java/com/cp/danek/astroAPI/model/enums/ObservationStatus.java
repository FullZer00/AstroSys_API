package com.cp.danek.astroAPI.model.enums;

public enum ObservationStatus {
    PLANNED("Запланировано"),
    IN_PROGRESS("В процессе"),
    COMPLETED("Завершено"),
    CANCELLED("Отменено");

    private final String displayName;

    ObservationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}