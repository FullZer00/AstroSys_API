package com.cp.danek.astroAPI.model.entities;

import com.cp.danek.astroAPI.model.enums.ObservationStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "observations")
public class Observation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "telescope_id", nullable = false)
    private Telescope telescope;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "astronomer_id", nullable = false)
    private User astronomer;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_object_id", nullable = false)
    private AstronomicalObject targetObject;

    @Column(name = "weather_conditions")
    private String weatherConditions;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ObservationStatus status = ObservationStatus.PLANNED;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "data_path")
    private String dataPath;

    // Связи
    @OneToMany(mappedBy = "observation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ObservationData> observationData = new ArrayList<>();

    // Конструкторы
    public Observation() {}

    public Observation(Telescope telescope, User astronomer, AstronomicalObject targetObject, LocalDateTime startTime) {
        this.telescope = telescope;
        this.astronomer = astronomer;
        this.targetObject = targetObject;
        this.startTime = startTime;
        this.status = ObservationStatus.PLANNED;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Telescope getTelescope() { return telescope; }
    public void setTelescope(Telescope telescope) { this.telescope = telescope; }

    public User getAstronomer() { return astronomer; }
    public void setAstronomer(User astronomer) { this.astronomer = astronomer; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public AstronomicalObject getTargetObject() { return targetObject; }
    public void setTargetObject(AstronomicalObject targetObject) { this.targetObject = targetObject; }

    public String getWeatherConditions() { return weatherConditions; }
    public void setWeatherConditions(String weatherConditions) { this.weatherConditions = weatherConditions; }

    public ObservationStatus getStatus() { return status; }
    public void setStatus(ObservationStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getDataPath() { return dataPath; }
    public void setDataPath(String dataPath) { this.dataPath = dataPath; }

    public List<ObservationData> getObservationData() { return observationData; }
    public void setObservationData(List<ObservationData> observationData) { this.observationData = observationData; }
}