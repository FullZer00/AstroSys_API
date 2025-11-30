package com.cp.danek.astroAPI.controller;

import com.cp.danek.astroAPI.dto.ApiResponseDTO;
import com.cp.danek.astroAPI.dto.CreateMaintenanceDTO;
import com.cp.danek.astroAPI.dto.MaintenanceDTO;
import com.cp.danek.astroAPI.model.entities.Maintenance;
import com.cp.danek.astroAPI.model.entities.Telescope;
import com.cp.danek.astroAPI.model.entities.User;
import com.cp.danek.astroAPI.model.enums.MaintenanceStatus;
import com.cp.danek.astroAPI.model.enums.MaintenanceType;
import com.cp.danek.astroAPI.service.MaintenanceService;
import com.cp.danek.astroAPI.service.TelescopeService;
import com.cp.danek.astroAPI.service.UserService;
import com.cp.danek.astroAPI.mapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/maintenances")
@CrossOrigin(origins = "*")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;
    private final TelescopeService telescopeService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public MaintenanceController(MaintenanceService maintenanceService, TelescopeService telescopeService,
                                 UserService userService, ModelMapper modelMapper) {
        this.maintenanceService = maintenanceService;
        this.telescopeService = telescopeService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    // GET /api/maintenances - получить все обслуживания
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<MaintenanceDTO>>> getAllMaintenances() {
        try {
            List<MaintenanceDTO> maintenances = maintenanceService.getAllMaintenances().stream()
                    .map(modelMapper::toMaintenanceDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Maintenances retrieved successfully", maintenances));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving maintenances: " + e.getMessage()));
        }
    }

    // GET /api/maintenances/{id} - получить обслуживание по ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<MaintenanceDTO>> getMaintenanceById(@PathVariable Long id) {
        try {
            Maintenance maintenance = maintenanceService.getMaintenanceById(id)
                    .orElseThrow(() -> new RuntimeException("Maintenance not found with id: " + id));
            return ResponseEntity.ok(ApiResponseDTO.success("Maintenance retrieved successfully", modelMapper.toMaintenanceDTO(maintenance)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving maintenance: " + e.getMessage()));
        }
    }

    // POST /api/maintenances - создать новое обслуживание
    @PostMapping
    public ResponseEntity<ApiResponseDTO<MaintenanceDTO>> createMaintenance(@RequestBody CreateMaintenanceDTO request) {
        try {
            // Получаем связанные сущности
            Telescope equipment = telescopeService.getTelescopeById(request.getEquipmentId())
                    .orElseThrow(() -> new RuntimeException("Telescope not found with id: " + request.getEquipmentId()));

            User engineer = userService.getUserById(request.getEngineerId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getEngineerId()));

            // Создаем обслуживание
            Maintenance maintenance = new Maintenance();
            maintenance.setEquipment(equipment);
            maintenance.setEngineer(engineer);
            maintenance.setMaintenanceType(request.getMaintenanceType());
            maintenance.setStartTime(request.getStartTime());
            maintenance.setEndTime(request.getEndTime());
            maintenance.setDescription(request.getDescription());
            maintenance.setStatus(MaintenanceStatus.SCHEDULED);

            Maintenance createdMaintenance = maintenanceService.createMaintenance(maintenance);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.success("Maintenance created successfully", modelMapper.toMaintenanceDTO(createdMaintenance)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error creating maintenance: " + e.getMessage()));
        }
    }

    // PUT /api/maintenances/{id} - обновить обслуживание
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<MaintenanceDTO>> updateMaintenance(@PathVariable Long id, @RequestBody CreateMaintenanceDTO request) {
        try {
            Maintenance existingMaintenance = maintenanceService.getMaintenanceById(id)
                    .orElseThrow(() -> new RuntimeException("Maintenance not found with id: " + id));

            // Обновляем данные
            existingMaintenance.setMaintenanceType(request.getMaintenanceType());
            existingMaintenance.setStartTime(request.getStartTime());
            existingMaintenance.setEndTime(request.getEndTime());
            existingMaintenance.setDescription(request.getDescription());

            Maintenance updatedMaintenance = maintenanceService.updateMaintenance(id, existingMaintenance);
            return ResponseEntity.ok(ApiResponseDTO.success("Maintenance updated successfully", modelMapper.toMaintenanceDTO(updatedMaintenance)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error updating maintenance: " + e.getMessage()));
        }
    }

    // DELETE /api/maintenances/{id} - удалить обслуживание
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteMaintenance(@PathVariable Long id) {
        try {
            maintenanceService.deleteMaintenance(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Maintenance deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error deleting maintenance: " + e.getMessage()));
        }
    }

    // GET /api/maintenances/telescope/{telescopeId} - обслуживания телескопа
    @GetMapping("/telescope/{telescopeId}")
    public ResponseEntity<ApiResponseDTO<List<MaintenanceDTO>>> getMaintenancesByTelescope(@PathVariable Long telescopeId) {
        try {
            List<MaintenanceDTO> maintenances = maintenanceService.getMaintenancesByTelescope(telescopeId).stream()
                    .map(modelMapper::toMaintenanceDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Maintenances retrieved successfully", maintenances));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving maintenances: " + e.getMessage()));
        }
    }

    // GET /api/maintenances/engineer/{engineerId} - обслуживания инженера
    @GetMapping("/engineer/{engineerId}")
    public ResponseEntity<ApiResponseDTO<List<MaintenanceDTO>>> getMaintenancesByEngineer(@PathVariable Long engineerId) {
        try {
            List<MaintenanceDTO> maintenances = maintenanceService.getMaintenancesByEngineer(engineerId).stream()
                    .map(modelMapper::toMaintenanceDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Maintenances retrieved successfully", maintenances));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving maintenances: " + e.getMessage()));
        }
    }

    // GET /api/maintenances/status/{status} - обслуживания по статусу
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponseDTO<List<MaintenanceDTO>>> getMaintenancesByStatus(@PathVariable MaintenanceStatus status) {
        try {
            List<MaintenanceDTO> maintenances = maintenanceService.getMaintenancesByStatus(status).stream()
                    .map(modelMapper::toMaintenanceDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Maintenances retrieved successfully", maintenances));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving maintenances: " + e.getMessage()));
        }
    }

    // GET /api/maintenances/type/{type} - обслуживания по типу
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponseDTO<List<MaintenanceDTO>>> getMaintenancesByType(@PathVariable MaintenanceType type) {
        try {
            List<MaintenanceDTO> maintenances = maintenanceService.getMaintenancesByType(type).stream()
                    .map(modelMapper::toMaintenanceDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Maintenances retrieved successfully", maintenances));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving maintenances: " + e.getMessage()));
        }
    }

    // PATCH /api/maintenances/{id}/start - начать обслуживание
    @PatchMapping("/{id}/start")
    public ResponseEntity<ApiResponseDTO<MaintenanceDTO>> startMaintenance(@PathVariable Long id) {
        try {
            maintenanceService.startMaintenance(id);
            Maintenance maintenance = maintenanceService.getMaintenanceById(id)
                    .orElseThrow(() -> new RuntimeException("Maintenance not found with id: " + id));
            return ResponseEntity.ok(ApiResponseDTO.success("Maintenance started successfully", modelMapper.toMaintenanceDTO(maintenance)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error starting maintenance: " + e.getMessage()));
        }
    }

    // PATCH /api/maintenances/{id}/complete - завершить обслуживание
    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponseDTO<MaintenanceDTO>> completeMaintenance(
            @PathVariable Long id,
            @RequestParam(required = false) String partsUsed,
            @RequestParam(required = false) Double cost) {
        try {
            maintenanceService.completeMaintenance(id, partsUsed, cost);
            Maintenance maintenance = maintenanceService.getMaintenanceById(id)
                    .orElseThrow(() -> new RuntimeException("Maintenance not found with id: " + id));
            return ResponseEntity.ok(ApiResponseDTO.success("Maintenance completed successfully", modelMapper.toMaintenanceDTO(maintenance)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error completing maintenance: " + e.getMessage()));
        }
    }

    // PATCH /api/maintenances/{id}/cancel - отменить обслуживание
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponseDTO<MaintenanceDTO>> cancelMaintenance(@PathVariable Long id) {
        try {
            maintenanceService.cancelMaintenance(id);
            Maintenance maintenance = maintenanceService.getMaintenanceById(id)
                    .orElseThrow(() -> new RuntimeException("Maintenance not found with id: " + id));
            return ResponseEntity.ok(ApiResponseDTO.success("Maintenance cancelled successfully", modelMapper.toMaintenanceDTO(maintenance)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error cancelling maintenance: " + e.getMessage()));
        }
    }

    // GET /api/maintenances/telescope/{telescopeId}/history - история обслуживания телескопа
    @GetMapping("/telescope/{telescopeId}/history")
    public ResponseEntity<ApiResponseDTO<List<MaintenanceDTO>>> getMaintenanceHistory(@PathVariable Long telescopeId) {
        try {
            List<MaintenanceDTO> maintenances = maintenanceService.getMaintenanceHistory(telescopeId).stream()
                    .map(modelMapper::toMaintenanceDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Maintenance history retrieved successfully", maintenances));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving maintenance history: " + e.getMessage()));
        }
    }

    // GET /api/maintenances/upcoming - предстоящие обслуживания
    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponseDTO<List<MaintenanceDTO>>> getUpcomingMaintenances() {
        try {
            List<MaintenanceDTO> maintenances = maintenanceService.getUpcomingMaintenances().stream()
                    .map(modelMapper::toMaintenanceDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Upcoming maintenances retrieved successfully", maintenances));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving upcoming maintenances: " + e.getMessage()));
        }
    }

    // GET /api/maintenances/active - активные обслуживания
    @GetMapping("/active")
    public ResponseEntity<ApiResponseDTO<List<MaintenanceDTO>>> getActiveMaintenances() {
        try {
            List<MaintenanceDTO> maintenances = maintenanceService.getActiveMaintenances().stream()
                    .map(modelMapper::toMaintenanceDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Active maintenances retrieved successfully", maintenances));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving active maintenances: " + e.getMessage()));
        }
    }

    // GET /api/maintenances/telescope/{telescopeId}/total-cost - общая стоимость обслуживания телескопа
    @GetMapping("/telescope/{telescopeId}/total-cost")
    public ResponseEntity<ApiResponseDTO<Double>> getTotalMaintenanceCost(@PathVariable Long telescopeId) {
        try {
            Double totalCost = maintenanceService.getTotalMaintenanceCost(telescopeId);
            return ResponseEntity.ok(ApiResponseDTO.success("Total maintenance cost retrieved successfully", totalCost));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving total maintenance cost: " + e.getMessage()));
        }
    }
}