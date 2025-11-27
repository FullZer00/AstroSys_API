package com.cp.danek.astroAPI.dto;

import com.cp.danek.astroAPI.model.enums.RequestStatus;

import java.time.LocalDateTime;

public class ObservationRequestDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long telescopeId;
    private String telescopeName;
    private Long targetObjectId;
    private String targetObjectName;
    private LocalDateTime requestedTime;
    private Double durationHours;
    private Integer priority;
    private RequestStatus status;
    private String scientificJustification;
    private LocalDateTime createdAt;
    private Long approvedById;
    private String approvedByName;

    public ObservationRequestDTO() {
    }

    public ObservationRequestDTO(Long id, Long userId, String userName, Long telescopeId, String telescopeName, Long targetObjectId, String targetObjectName, LocalDateTime requestedTime, Double durationHours, Integer priority, RequestStatus status, String scientificJustification, LocalDateTime createdAt, Long approvedById, String approvedByName) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.telescopeId = telescopeId;
        this.telescopeName = telescopeName;
        this.targetObjectId = targetObjectId;
        this.targetObjectName = targetObjectName;
        this.requestedTime = requestedTime;
        this.durationHours = durationHours;
        this.priority = priority;
        this.status = status;
        this.scientificJustification = scientificJustification;
        this.createdAt = createdAt;
        this.approvedById = approvedById;
        this.approvedByName = approvedByName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public LocalDateTime getRequestedTime() {
        return requestedTime;
    }

    public void setRequestedTime(LocalDateTime requestedTime) {
        this.requestedTime = requestedTime;
    }

    public Double getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(Double durationHours) {
        this.durationHours = durationHours;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getScientificJustification() {
        return scientificJustification;
    }

    public void setScientificJustification(String scientificJustification) {
        this.scientificJustification = scientificJustification;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getApprovedById() {
        return approvedById;
    }

    public void setApprovedById(Long approvedById) {
        this.approvedById = approvedById;
    }

    public String getApprovedByName() {
        return approvedByName;
    }

    public void setApprovedByName(String approvedByName) {
        this.approvedByName = approvedByName;
    }
}
