package com.cp.danek.astroAPI.model.entities;

import com.cp.danek.astroAPI.model.enums.AstronomicalObjectType;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "astronomical_objects")
public class AstronomicalObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AstronomicalObjectType type;

    @Column(name = "constellation")
    private String constellation;

    @Column(name = "coordinates")
    private String coordinates;

    @Column(name = "magnitude")
    private Double magnitude;

    @Column(name = "distance")
    private Double distance;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // Связи
    @OneToMany(mappedBy = "targetObject", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Observation> observations = new ArrayList<>();

    @OneToMany(mappedBy = "targetObject", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ObservationRequest> observationRequests = new ArrayList<>();

    // Конструкторы
    public AstronomicalObject() {}

    public AstronomicalObject(String name, AstronomicalObjectType type, String coordinates) {
        this.name = name;
        this.type = type;
        this.coordinates = coordinates;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public AstronomicalObjectType getType() { return type; }
    public void setType(AstronomicalObjectType type) { this.type = type; }

    public String getConstellation() { return constellation; }
    public void setConstellation(String constellation) { this.constellation = constellation; }

    public String getCoordinates() { return coordinates; }
    public void setCoordinates(String coordinates) { this.coordinates = coordinates; }

    public Double getMagnitude() { return magnitude; }
    public void setMagnitude(Double magnitude) { this.magnitude = magnitude; }

    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Observation> getObservations() { return observations; }
    public void setObservations(List<Observation> observations) { this.observations = observations; }

    public List<ObservationRequest> getObservationRequests() { return observationRequests; }
    public void setObservationRequests(List<ObservationRequest> observationRequests) { this.observationRequests = observationRequests; }
}