package com.cp.danek.astroAPI.model.repositories;

import com.cp.danek.astroAPI.model.entities.Telescope;
import com.cp.danek.astroAPI.model.enums.TelescopeStatus;
import com.cp.danek.astroAPI.model.enums.TelescopeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TelescopeRepository extends JpaRepository<Telescope, Long> {

    // Поиск по названию
    List<Telescope> findByNameContainingIgnoreCase(String name);

    // Поиск по типу телескопа
    List<Telescope> findByType(TelescopeType type);

    // Поиск по статусу
    List<Telescope> findByStatus(TelescopeStatus status);

    // Поиск доступных телескопов
    List<Telescope> findByStatusIn(List<TelescopeStatus> statuses);

    // Поиск телескопов с апертурой больше указанной
    List<Telescope> findByApertureGreaterThanEqual(Double minAperture);

    // Поиск по местоположению
    List<Telescope> findByLocationContainingIgnoreCase(String location);

    List<Telescope> findByLastMaintenanceDateIsNull();
    List<Telescope> findByLastMaintenanceDateBefore(LocalDate date);
}