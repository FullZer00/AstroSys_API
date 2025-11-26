package com.cp.danek.astroAPI.model.entities;

import com.cp.danek.astroAPI.model.enums.MaintenanceStatus;
import com.cp.danek.astroAPI.model.enums.MaintenanceType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenances")
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Telescope equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engineer_id", nullable = false)
    private User engineer;

    @Enumerated(EnumType.STRING)
    @Column(name = "maintenance_type", nullable = false)
    private MaintenanceType maintenanceType;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MaintenanceStatus status = MaintenanceStatus.SCHEDULED;

    @Column(name = "parts_used", columnDefinition = "TEXT")
    private String partsUsed;

    @Column(name = "cost")
    private Double cost;

    // Конструкторы
    public Maintenance() {}

    public Maintenance(Telescope equipment, User engineer, MaintenanceType maintenanceType, LocalDateTime startTime) {
        this.equipment = equipment;
        this.engineer = engineer;
        this.maintenanceType = maintenanceType;
        this.startTime = startTime;
        this.status = MaintenanceStatus.SCHEDULED;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Telescope getEquipment() { return equipment; }
    public void setEquipment(Telescope equipment) { this.equipment = equipment; }

    public User getEngineer() { return engineer; }
    public void setEngineer(User engineer) { this.engineer = engineer; }

    public MaintenanceType getMaintenanceType() { return maintenanceType; }
    public void setMaintenanceType(MaintenanceType maintenanceType) { this.maintenanceType = maintenanceType; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public MaintenanceStatus getStatus() { return status; }
    public void setStatus(MaintenanceStatus status) { this.status = status; }

    public String getPartsUsed() { return partsUsed; }
    public void setPartsUsed(String partsUsed) { this.partsUsed = partsUsed; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }
}