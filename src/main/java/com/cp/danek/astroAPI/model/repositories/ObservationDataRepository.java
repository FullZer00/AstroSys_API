package com.cp.danek.astroAPI.model.repositories;

import com.cp.danek.astroAPI.model.entities.Observation;
import com.cp.danek.astroAPI.model.entities.ObservationData;
import com.cp.danek.astroAPI.model.enums.DataQuality;
import com.cp.danek.astroAPI.model.enums.ProcessingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ObservationDataRepository extends JpaRepository<ObservationData, Long> {

    // Данные конкретного наблюдения
    List<ObservationData> findByObservation(Observation observation);

    // Данные по типу файла
    List<ObservationData> findByFileType(String fileType);

    // Данные по качеству
    List<ObservationData> findByDataQuality(DataQuality quality);

    // Данные по статусу обработки
    List<ObservationData> findByProcessingStatus(ProcessingStatus status);

    // Данные загруженные в указанный период
    List<ObservationData> findByUploadTimeBetween(LocalDateTime start, LocalDateTime end);

    // Популярные данные (часто скачиваемые)
    List<ObservationData> findByDownloadCountGreaterThanOrderByDownloadCountDesc(Integer minDownloadCount);

    // Данные с размером файла больше указанного
    List<ObservationData> findByFileSizeGreaterThanEqual(Long minFileSize);

    // Поиск по имени файла
    List<ObservationData> findByFileNameContainingIgnoreCase(String fileName);

    // Необработанные данные
    List<ObservationData> findByProcessingStatusOrderByUploadTimeAsc(ProcessingStatus status);

    // Статистика по данным наблюдения
    @Query("SELECT COUNT(d), SUM(d.fileSize), AVG(d.downloadCount) FROM ObservationData d WHERE d.observation = :observation")
    Object[] getObservationDataStats(@Param("observation") Observation observation);

    // Самые скачиваемые файлы
    @Query("SELECT d FROM ObservationData d ORDER BY d.downloadCount DESC")
    List<ObservationData> findMostDownloadedData();

    // Данные требующие обработки
    @Query("SELECT d FROM ObservationData d WHERE d.processingStatus = 'RAW' AND d.uploadTime < :cutoffTime")
    List<ObservationData> findDataNeedingProcessing(@Param("cutoffTime") LocalDateTime cutoffTime);
}