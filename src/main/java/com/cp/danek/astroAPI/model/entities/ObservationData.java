package com.cp.danek.astroAPI.model.entities;

import com.cp.danek.astroAPI.model.enums.DataQuality;
import com.cp.danek.astroAPI.model.enums.ProcessingStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "observation_data")
public class ObservationData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "observation_id", nullable = false)
    private Observation observation;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_type")
    private String fileType;

    @CreationTimestamp
    @Column(name = "upload_time", nullable = false, updatable = false)
    private LocalDateTime uploadTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_quality")
    private DataQuality dataQuality;

    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status")
    private ProcessingStatus processingStatus = ProcessingStatus.RAW;

    @Column(name = "download_count")
    private Integer downloadCount = 0;

    // Конструкторы
    public ObservationData() {}

    public ObservationData(Observation observation, String fileName, String fileType) {
        this.observation = observation;
        this.fileName = fileName;
        this.fileType = fileType;
        this.processingStatus = ProcessingStatus.RAW;
        this.downloadCount = 0;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Observation getObservation() { return observation; }
    public void setObservation(Observation observation) { this.observation = observation; }

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