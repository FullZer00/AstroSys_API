package com.cp.danek.astroAPI.service;

import com.cp.danek.astroAPI.model.entities.Maintenance;
import com.cp.danek.astroAPI.model.entities.Telescope;
import com.cp.danek.astroAPI.model.entities.User;
import com.cp.danek.astroAPI.model.enums.MaintenanceStatus;
import com.cp.danek.astroAPI.model.enums.MaintenanceType;
import com.cp.danek.astroAPI.model.repositories.MaintenanceRepository;
import com.cp.danek.astroAPI.model.repositories.TelescopeRepository;
import com.cp.danek.astroAPI.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final TelescopeRepository telescopeRepository;
    private final UserRepository userRepository;

    @Autowired
    public MaintenanceService(MaintenanceRepository maintenanceRepository,
                              TelescopeRepository telescopeRepository,
                              UserRepository userRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.telescopeRepository = telescopeRepository;
        this.userRepository = userRepository;
    }

    // Основные CRUD операции
    public List<Maintenance> getAllMaintenances() {
        return maintenanceRepository.findAll();
    }

    public Optional<Maintenance> getMaintenanceById(Long id) {
        return maintenanceRepository.findById(id);
    }

    public Maintenance createMaintenance(Maintenance maintenance) {
        return maintenanceRepository.save(maintenance);
    }

    public Maintenance updateMaintenance(Long id, Maintenance maintenanceDetails) {
        return maintenanceRepository.findById(id)
                .map(maintenance -> {
                    maintenance.setMaintenanceType(maintenanceDetails.getMaintenanceType());
                    maintenance.setStartTime(maintenanceDetails.getStartTime());
                    maintenance.setEndTime(maintenanceDetails.getEndTime());
                    maintenance.setDescription(maintenanceDetails.getDescription());
                    maintenance.setStatus(maintenanceDetails.getStatus());
                    maintenance.setPartsUsed(maintenanceDetails.getPartsUsed());
                    maintenance.setCost(maintenanceDetails.getCost());
                    return maintenanceRepository.save(maintenance);
                })
                .orElseThrow(() -> new RuntimeException("Обслуживание не найдено с id: " + id));
    }

    public void deleteMaintenance(Long id) {
        maintenanceRepository.deleteById(id);
    }

    // Бизнес-логика
    public List<Maintenance> getMaintenancesByTelescope(Long telescopeId) {
        Telescope telescope = telescopeRepository.findById(telescopeId)
                .orElseThrow(() -> new RuntimeException("Телескоп не найден с id: " + telescopeId));
        return maintenanceRepository.findByEquipment(telescope);
    }

    public List<Maintenance> getMaintenancesByEngineer(Long engineerId) {
        User engineer = userRepository.findById(engineerId)
                .orElseThrow(() -> new RuntimeException("Инженер не найден с id: " + engineerId));
        return maintenanceRepository.findByEngineer(engineer);
    }

    public List<Maintenance> getMaintenancesByStatus(MaintenanceStatus status) {
        return maintenanceRepository.findByStatus(status);
    }

    public List<Maintenance> getMaintenancesByType(MaintenanceType type) {
        return maintenanceRepository.findByMaintenanceType(type);
    }

    public List<Maintenance> getMaintenanceHistory(Long telescopeId) {
        Telescope telescope = telescopeRepository.findById(telescopeId)
                .orElseThrow(() -> new RuntimeException("Телескоп не найден с id: " + telescopeId));
        return maintenanceRepository.findByEquipmentOrderByStartTimeDesc(telescope);
    }

    public void startMaintenance(Long maintenanceId) {
        maintenanceRepository.findById(maintenanceId).ifPresent(maintenance -> {
            maintenance.setStatus(MaintenanceStatus.IN_PROGRESS);
            maintenance.setStartTime(LocalDateTime.now());
            maintenanceRepository.save(maintenance);
        });
    }

    public void completeMaintenance(Long maintenanceId, String partsUsed, Double cost) {
        maintenanceRepository.findById(maintenanceId).ifPresent(maintenance -> {
            maintenance.setStatus(MaintenanceStatus.COMPLETED);
            maintenance.setEndTime(LocalDateTime.now());
            maintenance.setPartsUsed(partsUsed);
            maintenance.setCost(cost);
            maintenanceRepository.save(maintenance);

            // Обновляем дату последнего обслуживания телескопа
            Telescope telescope = maintenance.getEquipment();
            telescope.setLastMaintenanceDate(LocalDateTime.now().toLocalDate());
            telescopeRepository.save(telescope);
        });
    }

    public void cancelMaintenance(Long maintenanceId) {
        maintenanceRepository.findById(maintenanceId).ifPresent(maintenance -> {
            maintenance.setStatus(MaintenanceStatus.CANCELLED);
            maintenanceRepository.save(maintenance);
        });
    }

    // Статистика и аналитика
    public Double getTotalMaintenanceCost(Long telescopeId) {
        Telescope telescope = telescopeRepository.findById(telescopeId)
                .orElseThrow(() -> new RuntimeException("Телескоп не найден с id: " + telescopeId));
        return maintenanceRepository.getTotalMaintenanceCostByEquipment(telescope);
    }

    public List<Maintenance> getUpcomingMaintenances() {
        return maintenanceRepository.findUpcomingPlannedMaintenance();
    }

    public List<Maintenance> getActiveMaintenances() {
        return maintenanceRepository.findByStatusAndStartTimeLessThanEqualAndEndTimeIsNull(
                MaintenanceStatus.IN_PROGRESS, LocalDateTime.now());
    }
}