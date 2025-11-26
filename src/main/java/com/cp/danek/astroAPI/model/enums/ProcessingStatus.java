package com.cp.danek.astroAPI.model.enums;

public enum ProcessingStatus {
    RAW("Сырые данные"),
    PROCESSED("Обработанные"),
    ANALYZED("Проанализированные");

    private final String displayName;

    ProcessingStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}