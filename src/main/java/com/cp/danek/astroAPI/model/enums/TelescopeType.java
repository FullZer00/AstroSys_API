package com.cp.danek.astroAPI.model.enums;

public enum TelescopeType {
    OPTICAL("Оптический"),
    RADIO("Радиотелескоп"),
    INFRARED("Инфракрасный"),
    ULTRAVIOLET("Ультрафиолетовый"),
    XRAY("Рентгеновский"),
    GAMMA("Гамма-телескоп");

    private final String displayName;

    TelescopeType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}