package com.cp.danek.astroAPI.model.enums;

public enum MaintenanceType {
    PLANNED("Плановое"),
    EMERGENCY("Аварийное"),
    CALIBRATION("Калибровка"),
    UPGRADE("Модернизация");

    private final String displayName;

    MaintenanceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}