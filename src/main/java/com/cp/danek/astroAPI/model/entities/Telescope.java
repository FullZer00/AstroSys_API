package com.cp.danek.astroAPI.model.entities;

import com.cp.danek.astroAPI.model.enums.TelescopeStatus;
import com.cp.danek.astroAPI.model.enums.TelescopeType;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "telescopes")
public class Telescope {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TelescopeType type;

    @Column(name = "aperture")
    private Double aperture; // диаметр в метрах

    @Column(name = "focal_length")
    private Double focalLength; // фокусное расстояние в мм

    @Column(name = "location")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TelescopeStatus status = TelescopeStatus.AVAILABLE;

    @Column(name = "last_maintenance_date")
    private LocalDate lastMaintenanceDate;

    @Column(name = "max_resolution")
    private String maxResolution;

    // Связи
    @OneToMany(mappedBy = "telescope", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Observation> observations = new ArrayList<>();

    @OneToMany(mappedBy = "telescope", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ObservationRequest> observationRequests = new ArrayList<>();

    @OneToMany(mappedBy = "equipment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Maintenance> maintenances = new ArrayList<>();

    // Конструкторы
    public Telescope() {}

    public Telescope(String name, TelescopeType type) {
        this.name = name;
        this.type = type;
        this.status = TelescopeStatus.AVAILABLE;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public TelescopeType getType() { return type; }
    public void setType(TelescopeType type) { this.type = type; }

    public Double getAperture() { return aperture; }
    public void setAperture(Double aperture) { this.aperture = aperture; }

    public Double getFocalLength() { return focalLength; }
    public void setFocalLength(Double focalLength) { this.focalLength = focalLength; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public TelescopeStatus getStatus() { return status; }
    public void setStatus(TelescopeStatus status) { this.status = status; }

    public LocalDate getLastMaintenanceDate() { return lastMaintenanceDate; }
    public void setLastMaintenanceDate(LocalDate lastMaintenanceDate) { this.lastMaintenanceDate = lastMaintenanceDate; }

    public String getMaxResolution() { return maxResolution; }
    public void setMaxResolution(String maxResolution) { this.maxResolution = maxResolution; }

    public List<Observation> getObservations() { return observations; }
    public void setObservations(List<Observation> observations) { this.observations = observations; }

    public List<ObservationRequest> getObservationRequests() { return observationRequests; }
    public void setObservationRequests(List<ObservationRequest> observationRequests) { this.observationRequests = observationRequests; }

    public List<Maintenance> getMaintenances() { return maintenances; }
    public void setMaintenances(List<Maintenance> maintenances) { this.maintenances = maintenances; }
}