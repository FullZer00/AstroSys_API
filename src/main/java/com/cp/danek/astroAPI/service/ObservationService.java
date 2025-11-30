package com.cp.danek.astroAPI.service;

import com.cp.danek.astroAPI.model.entities.Observation;
import com.cp.danek.astroAPI.model.entities.User;
import com.cp.danek.astroAPI.model.enums.ObservationStatus;
import com.cp.danek.astroAPI.model.repositories.AstronomicalObjectRepository;
import com.cp.danek.astroAPI.model.repositories.ObservationRepository;
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
public class ObservationService {

    private final ObservationRepository observationRepository;
    private final TelescopeRepository telescopeRepository;
    private final UserRepository userRepository;
    private final AstronomicalObjectRepository astronomicalObjectRepository;

    @Autowired
    public ObservationService(ObservationRepository observationRepository,
                              TelescopeRepository telescopeRepository,
                              UserRepository userRepository,
                              AstronomicalObjectRepository astronomicalObjectRepository) {
        this.observationRepository = observationRepository;
        this.telescopeRepository = telescopeRepository;
        this.userRepository = userRepository;
        this.astronomicalObjectRepository = astronomicalObjectRepository;
    }

    // Основные CRUD операции
    public List<Observation> getAllObservations() {
        return observationRepository.findAll();
    }

    public Optional<Observation> getObservationById(Long id) {
        return observationRepository.findById(id);
    }

    public Observation createObservation(Observation observation) {
        // Проверка доступности телескопа
        if (!isTelescopeAvailable(observation.getTelescope().getId(),
                observation.getStartTime(), observation.getEndTime())) {
            throw new RuntimeException("Телескоп недоступен в указанное время");
        }

        return observationRepository.save(observation);
    }

    public Observation updateObservation(Long id, Observation observationDetails) {
        return observationRepository.findById(id)
                .map(observation -> {
                    observation.setStartTime(observationDetails.getStartTime());
                    observation.setEndTime(observationDetails.getEndTime());
                    observation.setWeatherConditions(observationDetails.getWeatherConditions());
                    observation.setStatus(observationDetails.getStatus());
                    observation.setNotes(observationDetails.getNotes());
                    observation.setDataPath(observationDetails.getDataPath());
                    return observationRepository.save(observation);
                })
                .orElseThrow(() -> new RuntimeException("Наблюдение не найдено с id: " + id));
    }

    public void deleteObservation(Long id) {
        observationRepository.deleteById(id);
    }

    // Бизнес-логика
    public List<Observation> getObservationsByAstronomer(Long astronomerId) {
        User astronomer = userRepository.findById(astronomerId)
                .orElseThrow(() -> new RuntimeException("Астроном не найден с id: " + astronomerId));
        return observationRepository.findByAstronomer(astronomer);
    }

    public List<Observation> getObservationsByTelescope(Long telescopeId) {
        return observationRepository.findByTelescopeId(telescopeId);
    }

    public List<Observation> getObservationsByObject(Long objectId) {
        return observationRepository.findByTargetObjectId(objectId);
    }

    public List<Observation> getObservationsByStatus(ObservationStatus status) {
        return observationRepository.findByStatus(status);
    }

    public List<Observation> getObservationsInTimeRange(LocalDateTime start, LocalDateTime end) {
        return observationRepository.findByStartTimeBetween(start, end);
    }

    public void startObservation(Long observationId) {
        observationRepository.findById(observationId).ifPresent(observation -> {
            observation.setStatus(ObservationStatus.IN_PROGRESS);
            observationRepository.save(observation);
        });
    }

    public void completeObservation(Long observationId, String notes, String dataPath) {
        observationRepository.findById(observationId).ifPresent(observation -> {
            observation.setStatus(ObservationStatus.COMPLETED);
            observation.setEndTime(LocalDateTime.now());
            observation.setNotes(notes);
            observation.setDataPath(dataPath);
            observationRepository.save(observation);
        });
    }

    public void cancelObservation(Long observationId) {
        observationRepository.findById(observationId).ifPresent(observation -> {
            observation.setStatus(ObservationStatus.CANCELLED);
            observationRepository.save(observation);
        });
    }

    // ИСПРАВЛЕННАЯ бизнес-логика проверки доступности телескопа
    public boolean isTelescopeAvailable(Long telescopeId, LocalDateTime startTime, LocalDateTime endTime) {
        // Получаем все активные наблюдения для телескопа
        List<Observation> activeObservations = observationRepository.findByTelescopeId(telescopeId);

        // Проверяем пересечение временных интервалов
        for (Observation observation : activeObservations) {
            if (observation.getStatus() == ObservationStatus.IN_PROGRESS ||
                    observation.getStatus() == ObservationStatus.PLANNED) {

                // Проверяем пересечение временных интервалов
                if (isTimeOverlap(observation.getStartTime(), observation.getEndTime(), startTime, endTime)) {
                    return false; // Найдено пересечение - телескоп занят
                }
            }
        }

        return true; // Пересечений не найдено - телескоп доступен
    }

    // Вспомогательный метод для проверки пересечения временных интервалов
    private boolean isTimeOverlap(LocalDateTime start1, LocalDateTime end1,
                                  LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    public List<Observation> getActiveObservations() {
        LocalDateTime now = LocalDateTime.now();
        List<Observation> activeObservations = observationRepository.findByStatus(ObservationStatus.IN_PROGRESS);

        // Фильтруем наблюдения, которые действительно активны в данный момент
        return activeObservations.stream()
                .filter(obs -> obs.getStartTime() != null &&
                        obs.getEndTime() != null &&
                        !obs.getStartTime().isAfter(now) &&
                        !obs.getEndTime().isBefore(now))
                .toList();
    }

    // Дополнительные бизнес-методы
    public List<Observation> getUpcomingObservations() {
        LocalDateTime now = LocalDateTime.now();
        return observationRepository.findByStatusAndStartTimeAfter(
                ObservationStatus.PLANNED, now);
    }
}