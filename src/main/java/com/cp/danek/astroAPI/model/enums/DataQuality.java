package com.cp.danek.astroAPI.model.enums;

public enum DataQuality {
    EXCELLENT("Отличное"),
    GOOD("Хорошее"),
    POOR("Плохое");

    private final String displayName;

    DataQuality(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}