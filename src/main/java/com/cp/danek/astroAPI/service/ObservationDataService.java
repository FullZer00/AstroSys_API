package com.cp.danek.astroAPI.service;

import com.cp.danek.astroAPI.model.entities.ObservationData;
import com.cp.danek.astroAPI.model.entities.Observation;
import com.cp.danek.astroAPI.model.enums.DataQuality;
import com.cp.danek.astroAPI.model.enums.ProcessingStatus;
import com.cp.danek.astroAPI.model.repositories.ObservationDataRepository;
import com.cp.danek.astroAPI.model.repositories.ObservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ObservationDataService {

    private final ObservationDataRepository observationDataRepository;
    private final ObservationRepository observationRepository;

    @Autowired
    public ObservationDataService(ObservationDataRepository observationDataRepository,
                                  ObservationRepository observationRepository) {
        this.observationDataRepository = observationDataRepository;
        this.observationRepository = observationRepository;
    }

    // Основные CRUD операции
    public List<ObservationData> getAllData() {
        return observationDataRepository.findAll();
    }

    public Optional<ObservationData> getDataById(Long id) {
        return observationDataRepository.findById(id);
    }

    public ObservationData createData(ObservationData data) {
        return observationDataRepository.save(data);
    }

    public ObservationData updateData(Long id, ObservationData dataDetails) {
        return observationDataRepository.findById(id)
                .map(data -> {
                    data.setFileName(dataDetails.getFileName());
                    data.setFileSize(dataDetails.getFileSize());
                    data.setFileType(dataDetails.getFileType());
                    data.setDataQuality(dataDetails.getDataQuality());
                    data.setProcessingStatus(dataDetails.getProcessingStatus());
                    return observationDataRepository.save(data);
                })
                .orElseThrow(() -> new RuntimeException("Данные наблюдения не найдены с id: " + id));
    }

    public void deleteData(Long id) {
        observationDataRepository.deleteById(id);
    }

    // Бизнес-логика
    public List<ObservationData> getDataByObservation(Long observationId) {
        Observation observation = observationRepository.findById(observationId)
                .orElseThrow(() -> new RuntimeException("Наблюдение не найдено с id: " + observationId));
        return observationDataRepository.findByObservation(observation);
    }

    public List<ObservationData> getDataByFileType(String fileType) {
        return observationDataRepository.findByFileType(fileType);
    }

    public List<ObservationData> getDataByQuality(DataQuality quality) {
        return observationDataRepository.findByDataQuality(quality);
    }

    public List<ObservationData> getDataByProcessingStatus(ProcessingStatus status) {
        return observationDataRepository.findByProcessingStatus(status);
    }

    public List<ObservationData> getRawData() {
        return observationDataRepository.findByProcessingStatusOrderByUploadTimeAsc(ProcessingStatus.RAW);
    }

    public List<ObservationData> getPopularData() {
        return observationDataRepository.findMostDownloadedData();
    }

    public List<ObservationData> getLargeFiles(Long minSize) {
        return observationDataRepository.findByFileSizeGreaterThanEqual(minSize);
    }

    // Операции с данными
    public void incrementDownloadCount(Long dataId) {
        observationDataRepository.findById(dataId).ifPresent(data -> {
            data.setDownloadCount(data.getDownloadCount() + 1);
            observationDataRepository.save(data);
        });
    }

    public void updateProcessingStatus(Long dataId, ProcessingStatus status) {
        observationDataRepository.findById(dataId).ifPresent(data -> {
            data.setProcessingStatus(status);
            observationDataRepository.save(data);
        });
    }

    public void updateDataQuality(Long dataId, DataQuality quality) {
        observationDataRepository.findById(dataId).ifPresent(data -> {
            data.setDataQuality(quality);
            observationDataRepository.save(data);
        });
    }

    // Статистика
    public Object[] getObservationStats(Long observationId) {
        Observation observation = observationRepository.findById(observationId)
                .orElseThrow(() -> new RuntimeException("Наблюдение не найдено с id: " + observationId));
        return observationDataRepository.getObservationDataStats(observation);
    }

    public List<ObservationData> getDataNeedingProcessing() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24);
        return observationDataRepository.findDataNeedingProcessing(cutoffTime);
    }

    // Пакетные операции
    public void markAsProcessed(List<Long> dataIds) {
        dataIds.forEach(id -> {
            observationDataRepository.findById(id).ifPresent(data -> {
                data.setProcessingStatus(ProcessingStatus.PROCESSED);
                observationDataRepository.save(data);
            });
        });
    }
}