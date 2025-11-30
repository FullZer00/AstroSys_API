package com.cp.danek.astroAPI.dto;

import com.cp.danek.astroAPI.model.enums.MaintenanceStatus;
import com.cp.danek.astroAPI.model.enums.MaintenanceType;

import java.time.LocalDateTime;

public class MaintenanceDTO {
    private Long id;
    private Long equipmentId;
    private String equipmentName;
    private Long engineerId;
    private String engineerName;
    private MaintenanceType maintenanceType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private MaintenanceStatus status;
    private String partsUsed;
    private Double cost;

    // Конструкторы
    public MaintenanceDTO() {}

    public MaintenanceDTO(Long id, Long equipmentId, String equipmentName, Long engineerId, String engineerName,
                          MaintenanceType maintenanceType, LocalDateTime startTime, LocalDateTime endTime,
                          String description, MaintenanceStatus status, String partsUsed, Double cost) {
        this.id = id;
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.engineerId = engineerId;
        this.engineerName = engineerName;
        this.maintenanceType = maintenanceType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.status = status;
        this.partsUsed = partsUsed;
        this.cost = cost;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEquipmentId() { return equipmentId; }
    public void setEquipmentId(Long equipmentId) { this.equipmentId = equipmentId; }

    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }

    public Long getEngineerId() { return engineerId; }
    public void setEngineerId(Long engineerId) { this.engineerId = engineerId; }

    public String getEngineerName() { return engineerName; }
    public void setEngineerName(String engineerName) { this.engineerName = engineerName; }

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