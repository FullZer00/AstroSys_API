package com.cp.danek.astroAPI.model.enums;

public enum AstronomicalObjectType {
    STAR("Звезда"),
    GALAXY("Галактика"),
    NEBULA("Туманность"),
    PLANET("Планета"),
    ASTEROID("Астероид"),
    COMET("Комета"),
    CLUSTER("Звездное скопление"),
    QUASAR("Квазар"),
    BLACK_HOLE("Черная дыра");

    private final String displayName;

    AstronomicalObjectType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}