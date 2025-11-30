package com.cp.danek.astroAPI.dto;

import com.cp.danek.astroAPI.model.enums.TelescopeStatus;
import com.cp.danek.astroAPI.model.enums.TelescopeType;

import java.time.LocalDate;

public class TelescopeDTO {
    private Long id;
    private String name;
    private TelescopeType type;
    private Double aperture;
    private Double focalLength;
    private String location;
    private TelescopeStatus status;
    private LocalDate lastMaintenanceDate;
    private String maxResolution;

    public TelescopeDTO() {
    }

    public TelescopeDTO(Long id, String name, TelescopeType type, Double aperture, Double focalLength, String location, TelescopeStatus status, LocalDate lastMaintenanceDate, String maxResolution) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.aperture = aperture;
        this.focalLength = focalLength;
        this.location = location;
        this.status = status;
        this.lastMaintenanceDate = lastMaintenanceDate;
        this.maxResolution = maxResolution;
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

    public TelescopeType getType() {
        return type;
    }

    public void setType(TelescopeType type) {
        this.type = type;
    }

    public Double getAperture() {
        return aperture;
    }

    public void setAperture(Double aperture) {
        this.aperture = aperture;
    }

    public Double getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(Double focalLength) {
        this.focalLength = focalLength;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public TelescopeStatus getStatus() {
        return status;
    }

    public void setStatus(TelescopeStatus status) {
        this.status = status;
    }

    public LocalDate getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(LocalDate lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public String getMaxResolution() {
        return maxResolution;
    }

    public void setMaxResolution(String maxResolution) {
        this.maxResolution = maxResolution;
    }
}
