package com.cp.danek.astroAPI.dto;

import java.time.LocalDateTime;

public class CreateObservationRequestDTO {
    private Long userId;
    private Long telescopeId;
    private Long targetObjectId;
    private LocalDateTime requestedTime;
    private Double durationHours;
    private Integer priority;
    private String scientificJustification;

    // Конструкторы
    public CreateObservationRequestDTO() {}

    public CreateObservationRequestDTO(Long userId, Long telescopeId, Long targetObjectId,
                                       LocalDateTime requestedTime, Double durationHours,
                                       Integer priority, String scientificJustification) {
        this.userId = userId;
        this.telescopeId = telescopeId;
        this.targetObjectId = targetObjectId;
        this.requestedTime = requestedTime;
        this.durationHours = durationHours;
        this.priority = priority;
        this.scientificJustification = scientificJustification;
    }

    // Геттеры и сеттеры
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getTelescopeId() { return telescopeId; }
    public void setTelescopeId(Long telescopeId) { this.telescopeId = telescopeId; }

    public Long getTargetObjectId() { return targetObjectId; }
    public void setTargetObjectId(Long targetObjectId) { this.targetObjectId = targetObjectId; }

    public LocalDateTime getRequestedTime() { return requestedTime; }
    public void setRequestedTime(LocalDateTime requestedTime) { this.requestedTime = requestedTime; }

    public Double getDurationHours() { return durationHours; }
    public void setDurationHours(Double durationHours) { this.durationHours = durationHours; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public String getScientificJustification() { return scientificJustification; }
    public void setScientificJustification(String scientificJustification) { this.scientificJustification = scientificJustification; }
}