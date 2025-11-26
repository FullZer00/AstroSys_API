package com.cp.danek.astroAPI.model.entities;

import com.cp.danek.astroAPI.model.enums.RequestStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "observation_requests")
public class ObservationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "telescope_id", nullable = false)
    private Telescope telescope;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_object_id", nullable = false)
    private AstronomicalObject targetObject;

    @Column(name = "requested_time")
    private LocalDateTime requestedTime;

    @Column(name = "duration_hours")
    private Double durationHours;

    @Column(name = "priority")
    private Integer priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status = RequestStatus.PENDING;

    @Column(name = "scientific_justification", columnDefinition = "TEXT")
    private String scientificJustification;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    // Конструкторы
    public ObservationRequest() {}

    public ObservationRequest(User user, Telescope telescope, AstronomicalObject targetObject, LocalDateTime requestedTime) {
        this.user = user;
        this.telescope = telescope;
        this.targetObject = targetObject;
        this.requestedTime = requestedTime;
        this.status = RequestStatus.PENDING;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Telescope getTelescope() { return telescope; }
    public void setTelescope(Telescope telescope) { this.telescope = telescope; }

    public AstronomicalObject getTargetObject() { return targetObject; }
    public void setTargetObject(AstronomicalObject targetObject) { this.targetObject = targetObject; }

    public LocalDateTime getRequestedTime() { return requestedTime; }
    public void setRequestedTime(LocalDateTime requestedTime) { this.requestedTime = requestedTime; }

    public Double getDurationHours() { return durationHours; }
    public void setDurationHours(Double durationHours) { this.durationHours = durationHours; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public String getScientificJustification() { return scientificJustification; }
    public void setScientificJustification(String scientificJustification) { this.scientificJustification = scientificJustification; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getApprovedBy() { return approvedBy; }
    public void setApprovedBy(User approvedBy) { this.approvedBy = approvedBy; }
}