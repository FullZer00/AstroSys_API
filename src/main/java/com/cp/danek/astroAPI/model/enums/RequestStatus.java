package com.cp.danek.astroAPI.model.enums;

public enum RequestStatus {
    PENDING("На рассмотрении"),
    APPROVED("Одобрено"),
    REJECTED("Отклонено"),
    SCHEDULED("Запланировано");

    private final String displayName;

    RequestStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}