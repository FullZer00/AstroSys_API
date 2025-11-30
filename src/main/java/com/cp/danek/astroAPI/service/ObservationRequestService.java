package com.cp.danek.astroAPI.service;

import com.cp.danek.astroAPI.model.entities.ObservationRequest;
import com.cp.danek.astroAPI.model.entities.User;
import com.cp.danek.astroAPI.model.enums.RequestStatus;
import com.cp.danek.astroAPI.model.repositories.ObservationRequestRepository;
import com.cp.danek.astroAPI.model.repositories.UserRepository;
import com.cp.danek.astroAPI.model.repositories.TelescopeRepository;
import com.cp.danek.astroAPI.model.repositories.AstronomicalObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ObservationRequestService {

    private final ObservationRequestRepository observationRequestRepository;
    private final UserRepository userRepository;
    private final TelescopeRepository telescopeRepository;
    private final AstronomicalObjectRepository astronomicalObjectRepository;

    @Autowired
    public ObservationRequestService(ObservationRequestRepository observationRequestRepository,
                                     UserRepository userRepository,
                                     TelescopeRepository telescopeRepository,
                                     AstronomicalObjectRepository astronomicalObjectRepository) {
        this.observationRequestRepository = observationRequestRepository;
        this.userRepository = userRepository;
        this.telescopeRepository = telescopeRepository;
        this.astronomicalObjectRepository = astronomicalObjectRepository;
    }

    // Основные CRUD операции
    public List<ObservationRequest> getAllRequests() {
        return observationRequestRepository.findAll();
    }

    public Optional<ObservationRequest> getRequestById(Long id) {
        return observationRequestRepository.findById(id);
    }

    public ObservationRequest createRequest(ObservationRequest request) {
        return observationRequestRepository.save(request);
    }

    public ObservationRequest updateRequest(Long id, ObservationRequest requestDetails) {
        return observationRequestRepository.findById(id)
                .map(request -> {
                    request.setRequestedTime(requestDetails.getRequestedTime());
                    request.setDurationHours(requestDetails.getDurationHours());
                    request.setPriority(requestDetails.getPriority());
                    request.setScientificJustification(requestDetails.getScientificJustification());
                    return observationRequestRepository.save(request);
                })
                .orElseThrow(() -> new RuntimeException("Заявка не найдена с id: " + id));
    }

    public void deleteRequest(Long id) {
        observationRequestRepository.deleteById(id);
    }

    // Бизнес-логика
    public List<ObservationRequest> getRequestsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с id: " + userId));
        return observationRequestRepository.findByUser(user);
    }

    public List<ObservationRequest> getRequestsByStatus(RequestStatus status) {
        return observationRequestRepository.findByStatus(status);
    }

    public List<ObservationRequest> getPendingRequests() {
        return observationRequestRepository.findByStatusOrderByPriorityDescCreatedAtAsc(RequestStatus.PENDING);
    }

    public void approveRequest(Long requestId, Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Администратор не найден с id: " + adminId));

        observationRequestRepository.findById(requestId).ifPresent(request -> {
            request.setStatus(RequestStatus.APPROVED);
            request.setApprovedBy(admin);
            observationRequestRepository.save(request);
        });
    }

    public void rejectRequest(Long requestId) {
        observationRequestRepository.findById(requestId).ifPresent(request -> {
            request.setStatus(RequestStatus.REJECTED);
            observationRequestRepository.save(request);
        });
    }

    public void scheduleRequest(Long requestId) {
        observationRequestRepository.findById(requestId).ifPresent(request -> {
            request.setStatus(RequestStatus.SCHEDULED);
            observationRequestRepository.save(request);
        });
    }

    public List<ObservationRequest> getHighPriorityRequests(Integer minPriority) {
        return observationRequestRepository.findByPriorityGreaterThanEqual(minPriority);
    }

    // Статистика
    public Long getUserRequestCount(Long userId, RequestStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с id: " + userId));
        return observationRequestRepository.countByUserAndStatus(user, status);
    }
}