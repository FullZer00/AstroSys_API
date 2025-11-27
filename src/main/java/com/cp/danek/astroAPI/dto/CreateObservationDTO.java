package com.cp.danek.astroAPI.dto;

import java.time.LocalDateTime;

public class CreateObservationDTO {
    private Long telescopeId;
    private Long astronomerId;
    private Long targetObjectId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String weatherConditions;
    private String notes;

    public CreateObservationDTO() {
    }

    public CreateObservationDTO(Long telescopeId, Long astronomerId, Long targetObjectId, LocalDateTime startTime, LocalDateTime endTime, String weatherConditions, String notes) {
        this.telescopeId = telescopeId;
        this.astronomerId = astronomerId;
        this.targetObjectId = targetObjectId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.weatherConditions = weatherConditions;
        this.notes = notes;
    }

    public Long getTelescopeId() {
        return telescopeId;
    }

    public void setTelescopeId(Long telescopeId) {
        this.telescopeId = telescopeId;
    }

    public Long getAstronomerId() {
        return astronomerId;
    }

    public void setAstronomerId(Long astronomerId) {
        this.astronomerId = astronomerId;
    }

    public Long getTargetObjectId() {
        return targetObjectId;
    }

    public void setTargetObjectId(Long targetObjectId) {
        this.targetObjectId = targetObjectId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(String weatherConditions) {
        this.weatherConditions = weatherConditions;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
