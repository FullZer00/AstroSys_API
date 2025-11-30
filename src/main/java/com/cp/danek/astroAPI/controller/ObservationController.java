package com.cp.danek.astroAPI.controller;

import com.cp.danek.astroAPI.dto.ApiResponseDTO;
import com.cp.danek.astroAPI.dto.CreateObservationDTO;
import com.cp.danek.astroAPI.dto.ObservationDTO;
import com.cp.danek.astroAPI.mapper.ModelMapper;
import com.cp.danek.astroAPI.model.entities.AstronomicalObject;
import com.cp.danek.astroAPI.model.entities.Observation;
import com.cp.danek.astroAPI.model.entities.Telescope;
import com.cp.danek.astroAPI.model.entities.User;
import com.cp.danek.astroAPI.model.enums.ObservationStatus;
import com.cp.danek.astroAPI.service.AstronomicalObjectService;
import com.cp.danek.astroAPI.service.ObservationService;
import com.cp.danek.astroAPI.service.TelescopeService;
import com.cp.danek.astroAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/observations")
@CrossOrigin(origins = "*")
public class ObservationController {

    private final ObservationService observationService;
    private final TelescopeService telescopeService;
    private final UserService userService;
    private final AstronomicalObjectService astronomicalObjectService;
    private final ModelMapper modelMapper;

    @Autowired
    public ObservationController(ObservationService observationService, TelescopeService telescopeService,
                                 UserService userService, AstronomicalObjectService astronomicalObjectService,
                                 ModelMapper modelMapper) {
        this.observationService = observationService;
        this.telescopeService = telescopeService;
        this.userService = userService;
        this.astronomicalObjectService = astronomicalObjectService;
        this.modelMapper = modelMapper;
    }

    // GET /api/observations - получить все наблюдения
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ObservationDTO>>> getAllObservations() {
        try {
            List<ObservationDTO> observations = observationService.getAllObservations().stream()
                    .map(modelMapper::toObservationDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Observations retrieved successfully", observations));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving observations: " + e.getMessage()));
        }
    }

    // GET /api/observations/{id} - получить наблюдение по ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ObservationDTO>> getObservationById(@PathVariable Long id) {
        try {
            Observation observation = observationService.getObservationById(id)
                    .orElseThrow(() -> new RuntimeException("Observation not found with id: " + id));
            return ResponseEntity.ok(ApiResponseDTO.success("Observation retrieved successfully", modelMapper.toObservationDTO(observation)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving observation: " + e.getMessage()));
        }
    }

    // POST /api/observations - создать новое наблюдение
    @PostMapping
    public ResponseEntity<ApiResponseDTO<ObservationDTO>> createObservation(@RequestBody CreateObservationDTO request) {
        try {
            // Получаем связанные сущности
            Telescope telescope = telescopeService.getTelescopeById(request.getTelescopeId())
                    .orElseThrow(() -> new RuntimeException("Telescope not found with id: " + request.getTelescopeId()));

            User astronomer = userService.getUserById(request.getAstronomerId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getAstronomerId()));

            AstronomicalObject targetObject = astronomicalObjectService.getObjectById(request.getTargetObjectId())
                    .orElseThrow(() -> new RuntimeException("Astronomical object not found with id: " + request.getTargetObjectId()));

            // Создаем наблюдение
            Observation observation = new Observation();
            observation.setTelescope(telescope);
            observation.setAstronomer(astronomer);
            observation.setTargetObject(targetObject);
            observation.setStartTime(request.getStartTime());
            observation.setEndTime(request.getEndTime());
            observation.setWeatherConditions(request.getWeatherConditions());
            observation.setStatus(ObservationStatus.PLANNED);
            observation.setNotes(request.getNotes());

            Observation createdObservation = observationService.createObservation(observation);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.success("Observation created successfully", modelMapper.toObservationDTO(createdObservation)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error creating observation: " + e.getMessage()));
        }
    }

    // PUT /api/observations/{id} - обновить наблюдение
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ObservationDTO>> updateObservation(@PathVariable Long id, @RequestBody CreateObservationDTO request) {
        try {
            Observation existingObservation = observationService.getObservationById(id)
                    .orElseThrow(() -> new RuntimeException("Observation not found with id: " + id));

            // Обновляем данные
            existingObservation.setStartTime(request.getStartTime());
            existingObservation.setEndTime(request.getEndTime());
            existingObservation.setWeatherConditions(request.getWeatherConditions());
            existingObservation.setNotes(request.getNotes());

            Observation updatedObservation = observationService.updateObservation(id, existingObservation);
            return ResponseEntity.ok(ApiResponseDTO.success("Observation updated successfully", modelMapper.toObservationDTO(updatedObservation)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error updating observation: " + e.getMessage()));
        }
    }

    // DELETE /api/observations/{id} - удалить наблюдение
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteObservation(@PathVariable Long id) {
        try {
            observationService.deleteObservation(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Observation deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error deleting observation: " + e.getMessage()));
        }
    }

    // GET /api/observations/astronomer/{astronomerId} - наблюдения астронома
    @GetMapping("/astronomer/{astronomerId}")
    public ResponseEntity<ApiResponseDTO<List<ObservationDTO>>> getObservationsByAstronomer(@PathVariable Long astronomerId) {
        try {
            List<ObservationDTO> observations = observationService.getObservationsByAstronomer(astronomerId).stream()
                    .map(modelMapper::toObservationDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Observations retrieved successfully", observations));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving observations: " + e.getMessage()));
        }
    }

    // GET /api/observations/telescope/{telescopeId} - наблюдения телескопа
    @GetMapping("/telescope/{telescopeId}")
    public ResponseEntity<ApiResponseDTO<List<ObservationDTO>>> getObservationsByTelescope(@PathVariable Long telescopeId) {
        try {
            List<ObservationDTO> observations = observationService.getObservationsByTelescope(telescopeId).stream()
                    .map(modelMapper::toObservationDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Observations retrieved successfully", observations));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving observations: " + e.getMessage()));
        }
    }

    // GET /api/observations/object/{objectId} - наблюдения объекта
    @GetMapping("/object/{objectId}")
    public ResponseEntity<ApiResponseDTO<List<ObservationDTO>>> getObservationsByObject(@PathVariable Long objectId) {
        try {
            List<ObservationDTO> observations = observationService.getObservationsByObject(objectId).stream()
                    .map(modelMapper::toObservationDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Observations retrieved successfully", observations));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving observations: " + e.getMessage()));
        }
    }

    // GET /api/observations/status/{status} - наблюдения по статусу
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponseDTO<List<ObservationDTO>>> getObservationsByStatus(@PathVariable ObservationStatus status) {
        try {
            List<ObservationDTO> observations = observationService.getObservationsByStatus(status).stream()
                    .map(modelMapper::toObservationDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Observations retrieved successfully", observations));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving observations: " + e.getMessage()));
        }
    }

    // PATCH /api/observations/{id}/start - начать наблюдение
    @PatchMapping("/{id}/start")
    public ResponseEntity<ApiResponseDTO<ObservationDTO>> startObservation(@PathVariable Long id) {
        try {
            observationService.startObservation(id);
            Observation observation = observationService.getObservationById(id)
                    .orElseThrow(() -> new RuntimeException("Observation not found with id: " + id));
            return ResponseEntity.ok(ApiResponseDTO.success("Observation started successfully", modelMapper.toObservationDTO(observation)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error starting observation: " + e.getMessage()));
        }
    }

    // PATCH /api/observations/{id}/complete - завершить наблюдение
    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponseDTO<ObservationDTO>> completeObservation(
            @PathVariable Long id,
            @RequestParam(required = false) String notes,
            @RequestParam(required = false) String dataPath) {
        try {
            observationService.completeObservation(id, notes, dataPath);
            Observation observation = observationService.getObservationById(id)
                    .orElseThrow(() -> new RuntimeException("Observation not found with id: " + id));
            return ResponseEntity.ok(ApiResponseDTO.success("Observation completed successfully", modelMapper.toObservationDTO(observation)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error completing observation: " + e.getMessage()));
        }
    }

    // PATCH /api/observations/{id}/cancel - отменить наблюдение
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponseDTO<ObservationDTO>> cancelObservation(@PathVariable Long id) {
        try {
            observationService.cancelObservation(id);
            Observation observation = observationService.getObservationById(id)
                    .orElseThrow(() -> new RuntimeException("Observation not found with id: " + id));
            return ResponseEntity.ok(ApiResponseDTO.success("Observation cancelled successfully", modelMapper.toObservationDTO(observation)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error cancelling observation: " + e.getMessage()));
        }
    }

    // GET /api/observations/active - активные наблюдения
    @GetMapping("/active")
    public ResponseEntity<ApiResponseDTO<List<ObservationDTO>>> getActiveObservations() {
        try {
            List<ObservationDTO> observations = observationService.getActiveObservations().stream()
                    .map(modelMapper::toObservationDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Active observations retrieved successfully", observations));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving active observations: " + e.getMessage()));
        }
    }

    // GET /api/observations/upcoming - предстоящие наблюдения
    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponseDTO<List<ObservationDTO>>> getUpcomingObservations() {
        try {
            List<ObservationDTO> observations = observationService.getUpcomingObservations().stream()
                    .map(modelMapper::toObservationDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Upcoming observations retrieved successfully", observations));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving upcoming observations: " + e.getMessage()));
        }
    }
}