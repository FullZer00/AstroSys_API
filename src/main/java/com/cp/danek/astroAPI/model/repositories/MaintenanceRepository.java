package com.cp.danek.astroAPI.model.repositories;

import com.cp.danek.astroAPI.model.entities.Maintenance;
import com.cp.danek.astroAPI.model.entities.Telescope;
import com.cp.danek.astroAPI.model.entities.User;
import com.cp.danek.astroAPI.model.enums.MaintenanceStatus;
import com.cp.danek.astroAPI.model.enums.MaintenanceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    // Обслуживание конкретного оборудования
    List<Maintenance> findByEquipment(Telescope equipment);

    // Обслуживание, выполненное конкретным инженером
    List<Maintenance> findByEngineer(User engineer);

    // Обслуживание по типу
    List<Maintenance> findByMaintenanceType(MaintenanceType type);

    // Обслуживание по статусу
    List<Maintenance> findByStatus(MaintenanceStatus status);

    // Обслуживание в временном диапазоне
    List<Maintenance> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    // Активное обслуживание (в процессе)
    List<Maintenance> findByStatusAndStartTimeLessThanEqualAndEndTimeIsNull(MaintenanceStatus status, LocalDateTime time);

    // Завершенное обслуживание
    List<Maintenance> findByStatusAndEndTimeIsNotNull(MaintenanceStatus status);

    // Обслуживание с стоимостью выше указанной
    List<Maintenance> findByCostGreaterThanEqual(Double minCost);

    // История обслуживания оборудования с сортировкой
    List<Maintenance> findByEquipmentOrderByStartTimeDesc(Telescope equipment);

    // Статистика по стоимости обслуживания
    @Query("SELECT SUM(m.cost) FROM Maintenance m WHERE m.equipment = :equipment AND m.status = 'COMPLETED'")
    Double getTotalMaintenanceCostByEquipment(@Param("equipment") Telescope equipment);

    // Предстоящее плановое обслуживание
    @Query("SELECT m FROM Maintenance m WHERE m.status = 'SCHEDULED' AND m.maintenanceType = 'PLANNED' ORDER BY m.startTime ASC")
    List<Maintenance> findUpcomingPlannedMaintenance();
}