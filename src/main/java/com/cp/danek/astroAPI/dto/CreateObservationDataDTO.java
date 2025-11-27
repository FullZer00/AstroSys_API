package com.cp.danek.astroAPI.dto;

public class CreateObservationDataDTO {
    private Long observationId;
    private String fileName;
    private Long fileSize;
    private String fileType;

    // Конструкторы
    public CreateObservationDataDTO() {}

    public CreateObservationDataDTO(Long observationId, String fileName, Long fileSize, String fileType) {
        this.observationId = observationId;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
    }

    // Геттеры и сеттеры
    public Long getObservationId() { return observationId; }
    public void setObservationId(Long observationId) { this.observationId = observationId; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
}