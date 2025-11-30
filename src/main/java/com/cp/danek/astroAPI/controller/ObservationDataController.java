package com.cp.danek.astroAPI.controller;

import com.cp.danek.astroAPI.dto.ApiResponseDTO;
import com.cp.danek.astroAPI.dto.CreateObservationDataDTO;
import com.cp.danek.astroAPI.dto.ObservationDataDTO;
import com.cp.danek.astroAPI.mapper.ModelMapper;
import com.cp.danek.astroAPI.model.entities.Observation;
import com.cp.danek.astroAPI.model.entities.ObservationData;
import com.cp.danek.astroAPI.model.enums.DataQuality;
import com.cp.danek.astroAPI.model.enums.ProcessingStatus;
import com.cp.danek.astroAPI.service.ObservationDataService;
import com.cp.danek.astroAPI.service.ObservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/observation-data")
@CrossOrigin(origins = "*")
public class ObservationDataController {

    private final ObservationDataService observationDataService;
    private final ObservationService observationService;
    private final ModelMapper modelMapper;

    @Autowired
    public ObservationDataController(ObservationDataService observationDataService,
                                     ObservationService observationService,
                                     ModelMapper modelMapper) {
        this.observationDataService = observationDataService;
        this.observationService = observationService;
        this.modelMapper = modelMapper;
    }

    // GET /api/observation-data - получить все данные
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ObservationDataDTO>>> getAllData() {
        try {
            List<ObservationDataDTO> data = observationDataService.getAllData().stream()
                    .map(modelMapper::toObservationDataDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Observation data retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving observation data: " + e.getMessage()));
        }
    }

    // GET /api/observation-data/{id} - получить данные по ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ObservationDataDTO>> getDataById(@PathVariable Long id) {
        try {
            ObservationData data = observationDataService.getDataById(id)
                    .orElseThrow(() -> new RuntimeException("Observation data not found with id: " + id));
            return ResponseEntity.ok(ApiResponseDTO.success("Observation data retrieved successfully",
                    modelMapper.toObservationDataDTO(data)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving observation data: " + e.getMessage()));
        }
    }

    // POST /api/observation-data - создать новые данные
    @PostMapping
    public ResponseEntity<ApiResponseDTO<ObservationDataDTO>> createData(@RequestBody CreateObservationDataDTO request) {
        try {
            // Получаем связанное наблюдение
            Observation observation = observationService.getObservationById(request.getObservationId())
                    .orElseThrow(() -> new RuntimeException("Observation not found with id: " + request.getObservationId()));

            // Создаем данные
            ObservationData data = modelMapper.toObservationDataEntity(request);

            ObservationData createdData = observationDataService.createData(data);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.success("Observation data created successfully",
                            modelMapper.toObservationDataDTO(createdData)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error creating observation data: " + e.getMessage()));
        }
    }

    // PUT /api/observation-data/{id} - обновить данные
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ObservationDataDTO>> updateData(@PathVariable Long id, @RequestBody ObservationDataDTO dataDTO) {
        try {
            ObservationData data = new ObservationData();
            data.setFileName(dataDTO.getFileName());
            data.setFileSize(dataDTO.getFileSize());
            data.setFileType(dataDTO.getFileType());
            data.setDataQuality(dataDTO.getDataQuality());
            data.setProcessingStatus(dataDTO.getProcessingStatus());

            ObservationData updatedData = observationDataService.updateData(id, data);
            return ResponseEntity.ok(ApiResponseDTO.success("Observation data updated successfully",
                    modelMapper.toObservationDataDTO(updatedData)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error updating observation data: " + e.getMessage()));
        }
    }

    // DELETE /api/observation-data/{id} - удалить данные
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteData(@PathVariable Long id) {
        try {
            observationDataService.deleteData(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Observation data deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error deleting observation data: " + e.getMessage()));
        }
    }

    // GET /api/observation-data/observation/{observationId} - данные наблюдения
    @GetMapping("/observation/{observationId}")
    public ResponseEntity<ApiResponseDTO<List<ObservationDataDTO>>> getDataByObservation(@PathVariable Long observationId) {
        try {
            List<ObservationDataDTO> data = observationDataService.getDataByObservation(observationId).stream()
                    .map(modelMapper::toObservationDataDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Observation data retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving observation data: " + e.getMessage()));
        }
    }

    // GET /api/observation-data/file-type/{fileType} - данные по типу файла
    @GetMapping("/file-type/{fileType}")
    public ResponseEntity<ApiResponseDTO<List<ObservationDataDTO>>> getDataByFileType(@PathVariable String fileType) {
        try {
            List<ObservationDataDTO> data = observationDataService.getDataByFileType(fileType).stream()
                    .map(modelMapper::toObservationDataDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Observation data retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving observation data: " + e.getMessage()));
        }
    }

    // GET /api/observation-data/quality/{quality} - данные по качеству
    @GetMapping("/quality/{quality}")
    public ResponseEntity<ApiResponseDTO<List<ObservationDataDTO>>> getDataByQuality(@PathVariable DataQuality quality) {
        try {
            List<ObservationDataDTO> data = observationDataService.getDataByQuality(quality).stream()
                    .map(modelMapper::toObservationDataDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Observation data retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving observation data: " + e.getMessage()));
        }
    }

    // GET /api/observation-data/status/{status} - данные по статусу обработки
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponseDTO<List<ObservationDataDTO>>> getDataByProcessingStatus(@PathVariable ProcessingStatus status) {
        try {
            List<ObservationDataDTO> data = observationDataService.getDataByProcessingStatus(status).stream()
                    .map(modelMapper::toObservationDataDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Observation data retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving observation data: " + e.getMessage()));
        }
    }

    // GET /api/observation-data/raw - необработанные данные
    @GetMapping("/raw")
    public ResponseEntity<ApiResponseDTO<List<ObservationDataDTO>>> getRawData() {
        try {
            List<ObservationDataDTO> data = observationDataService.getRawData().stream()
                    .map(modelMapper::toObservationDataDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Raw observation data retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving raw observation data: " + e.getMessage()));
        }
    }

    // GET /api/observation-data/popular - популярные данные
    @GetMapping("/popular")
    public ResponseEntity<ApiResponseDTO<List<ObservationDataDTO>>> getPopularData() {
        try {
            List<ObservationDataDTO> data = observationDataService.getPopularData().stream()
                    .map(modelMapper::toObservationDataDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Popular observation data retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving popular observation data: " + e.getMessage()));
        }
    }

    // PATCH /api/observation-data/{id}/download - увеличить счетчик загрузок
    @PatchMapping("/{id}/download")
    public ResponseEntity<ApiResponseDTO<ObservationDataDTO>> incrementDownloadCount(@PathVariable Long id) {
        try {
            observationDataService.incrementDownloadCount(id);
            ObservationData data = observationDataService.getDataById(id)
                    .orElseThrow(() -> new RuntimeException("Observation data not found with id: " + id));
            return ResponseEntity.ok(ApiResponseDTO.success("Download count incremented successfully",
                    modelMapper.toObservationDataDTO(data)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error incrementing download count: " + e.getMessage()));
        }
    }

    // PATCH /api/observation-data/{id}/processing-status - изменить статус обработки
    @PatchMapping("/{id}/processing-status")
    public ResponseEntity<ApiResponseDTO<ObservationDataDTO>> updateProcessingStatus(
            @PathVariable Long id,
            @RequestParam ProcessingStatus status) {
        try {
            observationDataService.updateProcessingStatus(id, status);
            ObservationData data = observationDataService.getDataById(id)
                    .orElseThrow(() -> new RuntimeException("Observation data not found with id: " + id));
            return ResponseEntity.ok(ApiResponseDTO.success("Processing status updated successfully",
                    modelMapper.toObservationDataDTO(data)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error updating processing status: " + e.getMessage()));
        }
    }

    // PATCH /api/observation-data/{id}/quality - изменить качество данных
    @PatchMapping("/{id}/quality")
    public ResponseEntity<ApiResponseDTO<ObservationDataDTO>> updateDataQuality(
            @PathVariable Long id,
            @RequestParam DataQuality quality) {
        try {
            observationDataService.updateDataQuality(id, quality);
            ObservationData data = observationDataService.getDataById(id)
                    .orElseThrow(() -> new RuntimeException("Observation data not found with id: " + id));
            return ResponseEntity.ok(ApiResponseDTO.success("Data quality updated successfully",
                    modelMapper.toObservationDataDTO(data)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error updating data quality: " + e.getMessage()));
        }
    }

    // GET /api/observation-data/observation/{observationId}/stats - статистика данных наблюдения
    @GetMapping("/observation/{observationId}/stats")
    public ResponseEntity<ApiResponseDTO<Object>> getObservationStats(@PathVariable Long observationId) {
        try {
            Object[] stats = observationDataService.getObservationStats(observationId);

            var result = new Object() {
                public final Long fileCount = (Long) stats[0];
                public final Long totalSize = (Long) stats[1];
                public final Double avgDownloadCount = (Double) stats[2];
            };

            return ResponseEntity.ok(ApiResponseDTO.success("Observation statistics retrieved successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving observation statistics: " + e.getMessage()));
        }
    }

    // GET /api/observation-data/needs-processing - данные, требующие обработки
    @GetMapping("/needs-processing")
    public ResponseEntity<ApiResponseDTO<List<ObservationDataDTO>>> getDataNeedingProcessing() {
        try {
            List<ObservationDataDTO> data = observationDataService.getDataNeedingProcessing().stream()
                    .map(modelMapper::toObservationDataDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Data needing processing retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving data needing processing: " + e.getMessage()));
        }
    }

    // POST /api/observation-data/batch/mark-processed - пометить данные как обработанные
    @PostMapping("/batch/mark-processed")
    public ResponseEntity<ApiResponseDTO<Void>> markAsProcessed(@RequestBody List<Long> dataIds) {
        try {
            observationDataService.markAsProcessed(dataIds);
            return ResponseEntity.ok(ApiResponseDTO.success("Data marked as processed successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error marking data as processed: " + e.getMessage()));
        }
    }
}