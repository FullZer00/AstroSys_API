package com.cp.danek.astroAPI.controller;

import com.cp.danek.astroAPI.dto.ApiResponseDTO;
import com.cp.danek.astroAPI.dto.TelescopeDTO;
import com.cp.danek.astroAPI.mapper.ModelMapper;
import com.cp.danek.astroAPI.model.entities.Telescope;
import com.cp.danek.astroAPI.service.TelescopeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/telescopes")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ENGINEER')")
@Tag(name = "Телескопы", description = "API для управления телескопами")
public class TelescopeController {

    private final TelescopeService telescopeService;
    private final ModelMapper modelMapper;

    @Autowired
    public TelescopeController(TelescopeService telescopeService, ModelMapper modelMapper) {
        this.telescopeService = telescopeService;
        this.modelMapper = modelMapper;
    }

    // GET /api/telescopes - получить все телескопы
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<TelescopeDTO>>> getAllTelescopes() {
        try {
            List<TelescopeDTO> telescopes = telescopeService.getAllTelescopes().stream()
                    .map(modelMapper::toTelescopeDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Telescopes retrieved successfully", telescopes));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving telescopes: " + e.getMessage()));
        }
    }

    // GET /api/telescopes/{id} - получить телескоп по ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TelescopeDTO>> getTelescopeById(@PathVariable Long id) {
        try {
            Telescope telescope = telescopeService.getTelescopeById(id)
                    .orElseThrow(() -> new RuntimeException("Telescope not found with id: " + id));
            return ResponseEntity.ok(ApiResponseDTO.success("Telescope retrieved successfully", modelMapper.toTelescopeDTO(telescope)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving telescope: " + e.getMessage()));
        }
    }

    // POST /api/telescopes - создать новый телескоп
    @PostMapping
    public ResponseEntity<ApiResponseDTO<TelescopeDTO>> createTelescope(@RequestBody TelescopeDTO telescopeDTO) {
        try {
            Telescope telescope = modelMapper.toTelescopeEntity(telescopeDTO);
            Telescope createdTelescope = telescopeService.createTelescope(telescope);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.success("Telescope created successfully", modelMapper.toTelescopeDTO(createdTelescope)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error creating telescope: " + e.getMessage()));
        }
    }

    // PUT /api/telescopes/{id} - обновить телескоп
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TelescopeDTO>> updateTelescope(@PathVariable Long id, @RequestBody TelescopeDTO telescopeDTO) {
        try {
            Telescope existingTelescope = telescopeService.getTelescopeById(id)
                    .orElseThrow(() -> new RuntimeException("Telescope not found with id: " + id));

            modelMapper.updateTelescopeFromDTO(existingTelescope, telescopeDTO);
            Telescope updatedTelescope = telescopeService.updateTelescope(id, existingTelescope);
            return ResponseEntity.ok(ApiResponseDTO.success("Telescope updated successfully", modelMapper.toTelescopeDTO(updatedTelescope)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error updating telescope: " + e.getMessage()));
        }
    }

    // Остальные методы остаются аналогичными с использованием modelMapper...
    // [остальной код контроллера без изменений, только замена прямых маппингов на modelMapper]
}