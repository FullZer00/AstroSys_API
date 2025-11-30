package com.cp.danek.astroAPI.controller;

import com.cp.danek.astroAPI.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<ObservationDataDTO>>> getAllData() {
        try {
            List<ObservationDataDTO> data = observationDataService.getAllData().stream()
                    .map(modelMapper::toObservationDataDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Observation data retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving observation data: " + e.getMessage()));
        }
    }

    // GET /api/observation-data/{id} - получить данные по ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ObservationDataDTO>> getDataById(@PathVariable Long id) {
        try {
            ObservationData data = observationDataService.getDataById(id)
                    .orElseThrow(() -> new RuntimeException("Observation data not found with id: " + id));
            return ResponseEntity.ok(ApiResponse.success("Observation data retrieved successfully",
                    modelMapper.toObservationDataDTO(data)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving observation data: " + e.getMessage()));
        }
    }

    // POST /api/observation-data - создать новые данные
    @PostMapping
    public ResponseEntity<ApiResponse<ObservationDataDTO>> createData(@RequestBody CreateObservationDataDTO request) {
        try {
            // Получаем связанное наблюдение
            Observation observation = observationService.getObservationById(request.getObservationId())
                    .orElseThrow(() -> new RuntimeException("Observation not found with id: " + request.getObservationId()));

            // Создаем данные
            ObservationData data = modelMapper.toObservationDataEntity(request);

            ObservationData createdData = observationDataService.createData(data);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Observation data created successfully",
                            modelMapper.toObservationDataDTO(createdData)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error creating observation data: " + e.getMessage()));
        }
    }

    // PUT /api/observation-data/{id} - обновить данные
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ObservationDataDTO>> updateData(@PathVariable Long id, @RequestBody ObservationDataDTO dataDTO) {
        try {
            ObservationData data = new ObservationData();
            data.setFileName(dataDTO.getFileName());
            data.setFileSize(dataDTO.getFileSize());
            data.setFileType(dataDTO.getFileType());
            data.setDataQuality(dataDTO.getDataQuality());
            data.setProcessingStatus(dataDTO.getProcessingStatus());

            ObservationData updatedData = observationDataService.updateData(id, data);
            return ResponseEntity.ok(ApiResponse.success("Observation data updated successfully",
                    modelMapper.toObservationDataDTO(updatedData)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error updating observation data: " + e.getMessage()));
        }
    }

    // DELETE /api/observation-data/{id} - удалить данные
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteData(@PathVariable Long id) {
        try {
            observationDataService.deleteData(id);
            return ResponseEntity.ok(ApiResponse.success("Observation data deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error deleting observation data: " + e.getMessage()));
        }
    }

    // GET /api/observation-data/observation/{observationId} - данные наблюдения
    @GetMapping("/observation/{observationId}")
    public ResponseEntity<ApiResponse<List<ObservationDataDTO>>> getDataByObservation(@PathVariable Long observationId) {
        try {
            List<ObservationDataDTO> data = observationDataService.getDataByObservation(observationId).stream()
                    .map(modelMapper::toObservationDataDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Observation data retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving observation data: " + e.getMessage()));
        }
    }

    // GET /api/observation-data/file-type/{fileType} - данные по типу файла
    @GetMapping("/file-type/{fileType}")
    public ResponseEntity<ApiResponse<List<ObservationDataDTO>>> getDataByFileType(@PathVariable String fileType) {
        try {
            List<ObservationDataDTO> data = observationDataService.getDataByFileType(fileType).stream()
                    .map(modelMapper::toObservationDataDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Observation data retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving observation data: " + e.getMessage()));
        }
    }

    // GET /api/observation-data/quality/{quality} - данные по качеству
    @GetMapping("/quality/{quality}")
    public ResponseEntity<ApiResponse<List<ObservationDataDTO>>> getDataByQuality(@PathVariable DataQuality quality) {
        try {
            List<ObservationDataDTO> data = observationDataService.getDataByQuality(quality).stream()
                    .map(modelMapper::toObservationDataDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Observation data retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving observation data: " + e.getMessage()));
        }
    }

    // GET /api/observation-data/status/{status} - данные по статусу обработки
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<ObservationDataDTO>>> getDataByProcessingStatus(@PathVariable ProcessingStatus status) {
        try {
            List<ObservationDataDTO> data = observationDataService.getDataByProcessingStatus(status).stream()
                    .map(modelMapper::toObservationDataDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Observation data retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving observation data: " + e.getMessage()));
        }
    }

    // GET /api/observation-data/raw - необработанные данные
    @GetMapping("/raw")
    public ResponseEntity<ApiResponse<List<ObservationDataDTO>>> getRawData() {
        try {
            List<ObservationDataDTO> data = observationDataService.getRawData().stream()
                    .map(modelMapper::toObservationDataDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Raw observation data retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving raw observation data: " + e.getMessage()));
        }
    }

    // GET /api/observation-data/popular - популярные данные
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<ObservationDataDTO>>> getPopularData() {
        try {
            List<ObservationDataDTO> data = observationDataService.getPopularData().stream()
                    .map(modelMapper::toObservationDataDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Popular observation data retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving popular observation data: " + e.getMessage()));
        }
    }

    // PATCH /api/observation-data/{id}/download - увеличить счетчик загрузок
    @PatchMapping("/{id}/download")
    public ResponseEntity<ApiResponse<ObservationDataDTO>> incrementDownloadCount(@PathVariable Long id) {
        try {
            observationDataService.incrementDownloadCount(id);
            ObservationData data = observationDataService.getDataById(id)
                    .orElseThrow(() -> new RuntimeException("Observation data not found with id: " + id));
            return ResponseEntity.ok(ApiResponse.success("Download count incremented successfully",
                    modelMapper.toObservationDataDTO(data)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error incrementing download count: " + e.getMessage()));
        }
    }

    // PATCH /api/observation-data/{id}/processing-status - изменить статус обработки
    @PatchMapping("/{id}/processing-status")
    public ResponseEntity<ApiResponse<ObservationDataDTO>> updateProcessingStatus(
            @PathVariable Long id,
            @RequestParam ProcessingStatus status) {
        try {
            observationDataService.updateProcessingStatus(id, status);
            ObservationData data = observationDataService.getDataById(id)
                    .orElseThrow(() -> new RuntimeException("Observation data not found with id: " + id));
            return ResponseEntity.ok(ApiResponse.success("Processing status updated successfully",
                    modelMapper.toObservationDataDTO(data)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error updating processing status: " + e.getMessage()));
        }
    }

    // PATCH /api/observation-data/{id}/quality - изменить качество данных
    @PatchMapping("/{id}/quality")
    public ResponseEntity<ApiResponse<ObservationDataDTO>> updateDataQuality(
            @PathVariable Long id,
            @RequestParam DataQuality quality) {
        try {
            observationDataService.updateDataQuality(id, quality);
            ObservationData data = observationDataService.getDataById(id)
                    .orElseThrow(() -> new RuntimeException("Observation data not found with id: " + id));
            return ResponseEntity.ok(ApiResponse.success("Data quality updated successfully",
                    modelMapper.toObservationDataDTO(data)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error updating data quality: " + e.getMessage()));
        }
    }

    // GET /api/observation-data/observation/{observationId}/stats - статистика данных наблюдения
    @GetMapping("/observation/{observationId}/stats")
    public ResponseEntity<ApiResponse<Object>> getObservationStats(@PathVariable Long observationId) {
        try {
            Object[] stats = observationDataService.getObservationStats(observationId);

            var result = new Object() {
                public final Long fileCount = (Long) stats[0];
                public final Long totalSize = (Long) stats[1];
                public final Double avgDownloadCount = (Double) stats[2];
            };

            return ResponseEntity.ok(ApiResponse.success("Observation statistics retrieved successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving observation statistics: " + e.getMessage()));
        }
    }

    // GET /api/observation-data/needs-processing - данные, требующие обработки
    @GetMapping("/needs-processing")
    public ResponseEntity<ApiResponse<List<ObservationDataDTO>>> getDataNeedingProcessing() {
        try {
            List<ObservationDataDTO> data = observationDataService.getDataNeedingProcessing().stream()
                    .map(modelMapper::toObservationDataDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Data needing processing retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving data needing processing: " + e.getMessage()));
        }
    }

    // POST /api/observation-data/batch/mark-processed - пометить данные как обработанные
    @PostMapping("/batch/mark-processed")
    public ResponseEntity<ApiResponse<Void>> markAsProcessed(@RequestBody List<Long> dataIds) {
        try {
            observationDataService.markAsProcessed(dataIds);
            return ResponseEntity.ok(ApiResponse.success("Data marked as processed successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error marking data as processed: " + e.getMessage()));
        }
    }
}