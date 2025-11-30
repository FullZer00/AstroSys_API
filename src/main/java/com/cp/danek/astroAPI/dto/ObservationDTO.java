package com.cp.danek.astroAPI.dto;

import com.cp.danek.astroAPI.model.enums.ObservationStatus;

import java.time.LocalDateTime;

public class ObservationDTO {
    private Long id;
    private Long telescopeId;
    private String telescopeName;
    private Long astronomerId;
    private String astronomerName;
    private Long targetObjectId;
    private String targetObjectName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String weatherConditions;
    private ObservationStatus status;
    private String notes;
    private String dataPath;

    public ObservationDTO() {
    }

    public ObservationDTO(Long id, Long telescopeId, String telescopeName, Long astronomerId, String astronomerName, Long targetObjectId, String targetObjectName, LocalDateTime startTime, LocalDateTime endTime, String weatherConditions, ObservationStatus status, String notes, String dataPath) {
        this.id = id;
        this.telescopeId = telescopeId;
        this.telescopeName = telescopeName;
        this.astronomerId = astronomerId;
        this.astronomerName = astronomerName;
        this.targetObjectId = targetObjectId;
        this.targetObjectName = targetObjectName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.weatherConditions = weatherConditions;
        this.status = status;
        this.notes = notes;
        this.dataPath = dataPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTelescopeId() {
        return telescopeId;
    }

    public void setTelescopeId(Long telescopeId) {
        this.telescopeId = telescopeId;
    }

    public String getTelescopeName() {
        return telescopeName;
    }

    public void setTelescopeName(String telescopeName) {
        this.telescopeName = telescopeName;
    }

    public Long getAstronomerId() {
        return astronomerId;
    }

    public void setAstronomerId(Long astronomerId) {
        this.astronomerId = astronomerId;
    }

    public String getAstronomerName() {
        return astronomerName;
    }

    public void setAstronomerName(String astronomerName) {
        this.astronomerName = astronomerName;
    }

    public Long getTargetObjectId() {
        return targetObjectId;
    }

    public void setTargetObjectId(Long targetObjectId) {
        this.targetObjectId = targetObjectId;
    }

    public String getTargetObjectName() {
        return targetObjectName;
    }

    public void setTargetObjectName(String targetObjectName) {
        this.targetObjectName = targetObjectName;
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

    public ObservationStatus getStatus() {
        return status;
    }

    public void setStatus(ObservationStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }
}
