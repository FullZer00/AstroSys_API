package com.cp.danek.astroAPI.model.repositories;

import com.cp.danek.astroAPI.model.entities.ObservationRequest;
import com.cp.danek.astroAPI.model.entities.User;
import com.cp.danek.astroAPI.model.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ObservationRequestRepository extends JpaRepository<ObservationRequest, Long> {

    // Заявки конкретного пользователя
    List<ObservationRequest> findByUser(User user);

    // Заявки по телескопу
    List<ObservationRequest> findByTelescopeId(Long telescopeId);

    // Заявки по статусу
    List<ObservationRequest> findByStatus(RequestStatus status);

    // Заявки по приоритету
    List<ObservationRequest> findByPriority(Integer priority);

    // Заявки с приоритетом выше указанного
    List<ObservationRequest> findByPriorityGreaterThanEqual(Integer minPriority);

    // Заявки в временном диапазоне
    List<ObservationRequest> findByRequestedTimeBetween(LocalDateTime start, LocalDateTime end);

    // Заявки пользователя по статусу
    List<ObservationRequest> findByUserAndStatus(User user, RequestStatus status);

    // Необработанные заявки (ожидающие рассмотрения)
    List<ObservationRequest> findByStatusOrderByPriorityDescCreatedAtAsc(RequestStatus status);

    // Заявки, утвержденные конкретным администратором
    List<ObservationRequest> findByApprovedBy(User admin);

    // Статистика по заявкам пользователя
    @Query("SELECT COUNT(r) FROM ObservationRequest r WHERE r.user = :user AND r.status = :status")
    Long countByUserAndStatus(@Param("user") User user, @Param("status") RequestStatus status);

    // Поиск конфликтующих заявок по времени
    @Query("SELECT r FROM ObservationRequest r WHERE r.telescope.id = :telescopeId " +
            "AND r.status IN (com.cp.danek.astroAPI.model.enums.RequestStatus.PENDING, " +
            "com.cp.danek.astroAPI.model.enums.RequestStatus.APPROVED) " +
            "AND ((r.requestedTime BETWEEN :start AND :end) OR " +
            "(r.requestedTime <= :end AND FUNCTION('DATE_ADD', r.requestedTime, r.durationHours, 'HOUR') >= :start))")
    List<ObservationRequest> findConflictingRequests(@Param("telescopeId") Long telescopeId,
                                                     @Param("start") LocalDateTime start,
                                                     @Param("end") LocalDateTime end);
}