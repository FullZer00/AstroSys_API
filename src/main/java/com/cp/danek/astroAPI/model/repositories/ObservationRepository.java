package com.cp.danek.astroAPI.model.repositories;

import com.cp.danek.astroAPI.model.entities.Observation;
import com.cp.danek.astroAPI.model.entities.User;
import com.cp.danek.astroAPI.model.enums.ObservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ObservationRepository extends JpaRepository<Observation, Long> {

    // Наблюдения конкретного астронома
    List<Observation> findByAstronomer(User astronomer);

    // Наблюдения по телескопу
    List<Observation> findByTelescopeId(Long telescopeId);

    // Наблюдения по объекту
    List<Observation> findByTargetObjectId(Long objectId);

    // Наблюдения по статусу
    List<Observation> findByStatus(ObservationStatus status);

    // Наблюдения в временном диапазоне
    List<Observation> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    // Активные наблюдения (в процессе)
    List<Observation> findByStatusAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            ObservationStatus status, LocalDateTime time, LocalDateTime time2);

    // Поиск наблюдений по астроному и статусу
    List<Observation> findByAstronomerAndStatus(User astronomer, ObservationStatus status);

    // Статистика по наблюдениям
    @Query("SELECT COUNT(o) FROM Observation o WHERE o.astronomer = :astronomer AND o.status = :status")
    Long countByAstronomerAndStatus(@Param("astronomer") User astronomer, @Param("status") ObservationStatus status);

    // Проверка доступности телескопа в указанное время
    @Query("SELECT COUNT(o) FROM Observation o WHERE o.telescope.id = :telescopeId " +
            "AND o.status IN (com.cp.danek.astroAPI.model.enums.ObservationStatus.IN_PROGRESS, " +
            "com.cp.danek.astroAPI.model.enums.ObservationStatus.PLANNED) " +
            "AND ((o.startTime BETWEEN :start AND :end) OR (o.endTime BETWEEN :start AND :end) " +
            "OR (o.startTime <= :start AND o.endTime >= :end))")
    Long countConflictingObservations(@Param("telescopeId") Long telescopeId,
                                      @Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end);
}