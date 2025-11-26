package com.cp.danek.astroAPI.model.entities;

import com.cp.danek.astroAPI.model.enums.UserRole;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Связи
    @OneToMany(mappedBy = "astronomer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Observation> observations = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ObservationRequest> observationRequests = new ArrayList<>();

    @OneToMany(mappedBy = "approvedBy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ObservationRequest> approvedRequests = new ArrayList<>();

    @OneToMany(mappedBy = "engineer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Maintenance> maintenances = new ArrayList<>();

    // Конструкторы
    public User() {}

    public User(String login, String passwordHash, Role role, String fullName) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.role = role;
        this.fullName = fullName;
        this.isActive = true;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public List<Observation> getObservations() { return observations; }
    public void setObservations(List<Observation> observations) { this.observations = observations; }

    public List<ObservationRequest> getObservationRequests() { return observationRequests; }
    public void setObservationRequests(List<ObservationRequest> observationRequests) { this.observationRequests = observationRequests; }

    public List<ObservationRequest> getApprovedRequests() { return approvedRequests; }
    public void setApprovedRequests(List<ObservationRequest> approvedRequests) { this.approvedRequests = approvedRequests; }

    public List<Maintenance> getMaintenances() { return maintenances; }
    public void setMaintenances(List<Maintenance> maintenances) { this.maintenances = maintenances; }
}