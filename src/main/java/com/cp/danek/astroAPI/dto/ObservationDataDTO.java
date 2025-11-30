package com.cp.danek.astroAPI.dto;

import com.cp.danek.astroAPI.model.enums.DataQuality;
import com.cp.danek.astroAPI.model.enums.ProcessingStatus;

import java.time.LocalDateTime;

public class ObservationDataDTO {
    private Long id;
    private Long observationId;
    private String fileName;
    private Long fileSize;
    private String fileType;
    private LocalDateTime uploadTime;
    private DataQuality dataQuality;
    private ProcessingStatus processingStatus;
    private Integer downloadCount;

    // Конструкторы
    public ObservationDataDTO() {}

    public ObservationDataDTO(Long id, Long observationId, String fileName, Long fileSize, String fileType,
                              LocalDateTime uploadTime, DataQuality dataQuality, ProcessingStatus processingStatus,
                              Integer downloadCount) {
        this.id = id;
        this.observationId = observationId;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.uploadTime = uploadTime;
        this.dataQuality = dataQuality;
        this.processingStatus = processingStatus;
        this.downloadCount = downloadCount;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getObservationId() { return observationId; }
    public void setObservationId(Long observationId) { this.observationId = observationId; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public LocalDateTime getUploadTime() { return uploadTime; }
    public void setUploadTime(LocalDateTime uploadTime) { this.uploadTime = uploadTime; }

    public DataQuality getDataQuality() { return dataQuality; }
    public void setDataQuality(DataQuality dataQuality) { this.dataQuality = dataQuality; }

    public ProcessingStatus getProcessingStatus() { return processingStatus; }
    public void setProcessingStatus(ProcessingStatus processingStatus) { this.processingStatus = processingStatus; }

    public Integer getDownloadCount() { return downloadCount; }
    public void setDownloadCount(Integer downloadCount) { this.downloadCount = downloadCount; }
}