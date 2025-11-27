package com.cp.danek.astroAPI.dto;

import com.cp.danek.astroAPI.model.enums.MaintenanceType;

import java.time.LocalDateTime;

public class CreateMaintenanceDTO {
    private Long equipmentId;
    private Long engineerId;
    private MaintenanceType maintenanceType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;

    // Конструкторы
    public CreateMaintenanceDTO() {}

    public CreateMaintenanceDTO(Long equipmentId, Long engineerId, MaintenanceType maintenanceType,
                                    LocalDateTime startTime, LocalDateTime endTime, String description) {
        this.equipmentId = equipmentId;
        this.engineerId = engineerId;
        this.maintenanceType = maintenanceType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    // Геттеры и сеттеры
    public Long getEquipmentId() { return equipmentId; }
    public void setEquipmentId(Long equipmentId) { this.equipmentId = equipmentId; }

    public Long getEngineerId() { return engineerId; }
    public void setEngineerId(Long engineerId) { this.engineerId = engineerId; }

    public MaintenanceType getMaintenanceType() { return maintenanceType; }
    public void setMaintenanceType(MaintenanceType maintenanceType) { this.maintenanceType = maintenanceType; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}