package com.cp.danek.astroAPI.controller;

import com.cp.danek.astroAPI.dto.ApiResponse;
import com.cp.danek.astroAPI.dto.CreateObservationRequestDTO;
import com.cp.danek.astroAPI.dto.ObservationRequestDTO;
import com.cp.danek.astroAPI.mapper.ModelMapper;
import com.cp.danek.astroAPI.model.entities.AstronomicalObject;
import com.cp.danek.astroAPI.model.entities.ObservationRequest;
import com.cp.danek.astroAPI.model.entities.Telescope;
import com.cp.danek.astroAPI.model.entities.User;
import com.cp.danek.astroAPI.model.enums.RequestStatus;
import com.cp.danek.astroAPI.service.AstronomicalObjectService;
import com.cp.danek.astroAPI.service.ObservationRequestService;
import com.cp.danek.astroAPI.service.TelescopeService;
import com.cp.danek.astroAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/observation-requests")
@CrossOrigin(origins = "*")
public class ObservationRequestController {

    private final ObservationRequestService observationRequestService;
    private final UserService userService;
    private final TelescopeService telescopeService;
    private final AstronomicalObjectService astronomicalObjectService;
    private final ModelMapper modelMapper;

    @Autowired
    public ObservationRequestController(ObservationRequestService observationRequestService,
                                        UserService userService, TelescopeService telescopeService,
                                        AstronomicalObjectService astronomicalObjectService,
                                        ModelMapper modelMapper) {
        this.observationRequestService = observationRequestService;
        this.userService = userService;
        this.telescopeService = telescopeService;
        this.astronomicalObjectService = astronomicalObjectService;
        this.modelMapper = modelMapper;
    }

    // GET /api/observation-requests - получить все заявки
    @GetMapping
    public ResponseEntity<ApiResponse<List<ObservationRequestDTO>>> getAllRequests() {
        try {
            List<ObservationRequestDTO> requests = observationRequestService.getAllRequests().stream()
                    .map(modelMapper::toObservationRequestDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Observation requests retrieved successfully", requests));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving observation requests: " + e.getMessage()));
        }
    }

    // GET /api/observation-requests/{id} - получить заявку по ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ObservationRequestDTO>> getRequestById(@PathVariable Long id) {
        try {
            ObservationRequest request = observationRequestService.getRequestById(id)
                    .orElseThrow(() -> new RuntimeException("Observation request not found with id: " + id));
            return ResponseEntity.ok(ApiResponse.success("Observation request retrieved successfully", modelMapper.toObservationRequestDTO(request)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving observation request: " + e.getMessage()));
        }
    }

    // POST /api/observation-requests - создать новую заявку
    @PostMapping
    public ResponseEntity<ApiResponse<ObservationRequestDTO>> createRequest(@RequestBody CreateObservationRequestDTO requestDTO) {
        try {
            // Получаем связанные сущности
            User user = userService.getUserById(requestDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + requestDTO.getUserId()));

            Telescope telescope = telescopeService.getTelescopeById(requestDTO.getTelescopeId())
                    .orElseThrow(() -> new RuntimeException("Telescope not found with id: " + requestDTO.getTelescopeId()));

            AstronomicalObject targetObject = astronomicalObjectService.getObjectById(requestDTO.getTargetObjectId())
                    .orElseThrow(() -> new RuntimeException("Astronomical object not found with id: " + requestDTO.getTargetObjectId()));

            // Создаем заявку
            ObservationRequest request = new ObservationRequest();
            request.setUser(user);
            request.setTelescope(telescope);
            request.setTargetObject(targetObject);
            request.setRequestedTime(requestDTO.getRequestedTime());
            request.setDurationHours(requestDTO.getDurationHours());
            request.setPriority(requestDTO.getPriority());
            request.setScientificJustification(requestDTO.getScientificJustification());
            request.setStatus(RequestStatus.PENDING);

            ObservationRequest createdRequest = observationRequestService.createRequest(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Observation request created successfully", modelMapper.toObservationRequestDTO(createdRequest)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error creating observation request: " + e.getMessage()));
        }
    }

    // PUT /api/observation-requests/{id} - обновить заявку
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ObservationRequestDTO>> updateRequest(@PathVariable Long id, @RequestBody CreateObservationRequestDTO requestDTO) {
        try {
            ObservationRequest existingRequest = observationRequestService.getRequestById(id)
                    .orElseThrow(() -> new RuntimeException("Observation request not found with id: " + id));

            // Обновляем данные
            existingRequest.setRequestedTime(requestDTO.getRequestedTime());
            existingRequest.setDurationHours(requestDTO.getDurationHours());
            existingRequest.setPriority(requestDTO.getPriority());
            existingRequest.setScientificJustification(requestDTO.getScientificJustification());

            ObservationRequest updatedRequest = observationRequestService.updateRequest(id, existingRequest);
            return ResponseEntity.ok(ApiResponse.success("Observation request updated successfully", modelMapper.toObservationRequestDTO(updatedRequest)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error updating observation request: " + e.getMessage()));
        }
    }

    // DELETE /api/observation-requests/{id} - удалить заявку
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRequest(@PathVariable Long id) {
        try {
            observationRequestService.deleteRequest(id);
            return ResponseEntity.ok(ApiResponse.success("Observation request deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error deleting observation request: " + e.getMessage()));
        }
    }

    // GET /api/observation-requests/user/{userId} - заявки пользователя
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ObservationRequestDTO>>> getRequestsByUser(@PathVariable Long userId) {
        try {
            List<ObservationRequestDTO> requests = observationRequestService.getRequestsByUser(userId).stream()
                    .map(modelMapper::toObservationRequestDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Observation requests retrieved successfully", requests));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving observation requests: " + e.getMessage()));
        }
    }

    // GET /api/observation-requests/status/{status} - заявки по статусу
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<ObservationRequestDTO>>> getRequestsByStatus(@PathVariable RequestStatus status) {
        try {
            List<ObservationRequestDTO> requests = observationRequestService.getRequestsByStatus(status).stream()
                    .map(modelMapper::toObservationRequestDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Observation requests retrieved successfully", requests));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving observation requests: " + e.getMessage()));
        }
    }

    // GET /api/observation-requests/pending - необработанные заявки
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<ObservationRequestDTO>>> getPendingRequests() {
        try {
            List<ObservationRequestDTO> requests = observationRequestService.getPendingRequests().stream()
                    .map(modelMapper::toObservationRequestDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Pending observation requests retrieved successfully", requests));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving pending observation requests: " + e.getMessage()));
        }
    }

    // PATCH /api/observation-requests/{id}/approve - утвердить заявку
    @PatchMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<ObservationRequestDTO>> approveRequest(
            @PathVariable Long id,
            @RequestParam Long adminId) {
        try {
            observationRequestService.approveRequest(id, adminId);
            ObservationRequest request = observationRequestService.getRequestById(id)
                    .orElseThrow(() -> new RuntimeException("Observation request not found with id: " + id));
            return ResponseEntity.ok(ApiResponse.success("Observation request approved successfully", modelMapper.toObservationRequestDTO(request)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error approving observation request: " + e.getMessage()));
        }
    }

    // PATCH /api/observation-requests/{id}/reject - отклонить заявку
    @PatchMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<ObservationRequestDTO>> rejectRequest(@PathVariable Long id) {
        try {
            observationRequestService.rejectRequest(id);
            ObservationRequest request = observationRequestService.getRequestById(id)
                    .orElseThrow(() -> new RuntimeException("Observation request not found with id: " + id));
            return ResponseEntity.ok(ApiResponse.success("Observation request rejected successfully", modelMapper.toObservationRequestDTO(request)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error rejecting observation request: " + e.getMessage()));
        }
    }

    // PATCH /api/observation-requests/{id}/schedule - запланировать заявку
    @PatchMapping("/{id}/schedule")
    public ResponseEntity<ApiResponse<ObservationRequestDTO>> scheduleRequest(@PathVariable Long id) {
        try {
            observationRequestService.scheduleRequest(id);
            ObservationRequest request = observationRequestService.getRequestById(id)
                    .orElseThrow(() -> new RuntimeException("Observation request not found with id: " + id));
            return ResponseEntity.ok(ApiResponse.success("Observation request scheduled successfully", modelMapper.toObservationRequestDTO(request)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error scheduling observation request: " + e.getMessage()));
        }
    }

    // GET /api/observation-requests/high-priority - заявки с высоким приоритетом
    @GetMapping("/high-priority")
    public ResponseEntity<ApiResponse<List<ObservationRequestDTO>>> getHighPriorityRequests(
            @RequestParam(defaultValue = "4") Integer minPriority) {
        try {
            List<ObservationRequestDTO> requests = observationRequestService.getHighPriorityRequests(minPriority).stream()
                    .map(modelMapper::toObservationRequestDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("High priority observation requests retrieved successfully", requests));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving high priority observation requests: " + e.getMessage()));
        }
    }

    // GET /api/observation-requests/user/{userId}/stats - статистика заявок пользователя
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<ApiResponse<Object>> getUserRequestStats(@PathVariable Long userId) {
        try {
            Long pendingCount = observationRequestService.getUserRequestCount(userId, RequestStatus.PENDING);
            Long approvedCount = observationRequestService.getUserRequestCount(userId, RequestStatus.APPROVED);
            Long rejectedCount = observationRequestService.getUserRequestCount(userId, RequestStatus.REJECTED);

            var stats = new Object() {
                public final Long pending = pendingCount;
                public final Long approved = approvedCount;
                public final Long rejected = rejectedCount;
                public final Long total = pendingCount + approvedCount + rejectedCount;
            };

            return ResponseEntity.ok(ApiResponse.success("User request statistics retrieved successfully", stats));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving user request statistics: " + e.getMessage()));
        }
    }
}