package com.cp.danek.astroAPI.dto;

import com.cp.danek.astroAPI.model.enums.AstronomicalObjectType;

public class AstronomicalObjectDTO {
    private Long id;
    private String name;
    private AstronomicalObjectType type;
    private String constellation;
    private String coordinates;
    private Double magnitude;
    private Double distance;
    private String description;

    public AstronomicalObjectDTO() {}

    public AstronomicalObjectDTO(Long id, String name, AstronomicalObjectType type, String constellation, String coordinates, Double magnitude, Double distance, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.constellation = constellation;
        this.coordinates = coordinates;
        this.magnitude = magnitude;
        this.distance = distance;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AstronomicalObjectType getType() {
        return type;
    }

    public void setType(AstronomicalObjectType type) {
        this.type = type;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public Double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Double magnitude) {
        this.magnitude = magnitude;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
