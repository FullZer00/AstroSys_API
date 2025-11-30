package com.cp.danek.astroAPI.controller;

import com.cp.danek.astroAPI.dto.ApiResponse;
import com.cp.danek.astroAPI.dto.AstronomicalObjectDTO;
import com.cp.danek.astroAPI.model.entities.AstronomicalObject;
import com.cp.danek.astroAPI.model.enums.AstronomicalObjectType;
import com.cp.danek.astroAPI.service.AstronomicalObjectService;
import com.cp.danek.astroAPI.mapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/astronomical-objects")
@CrossOrigin(origins = "*")
public class AstronomicalObjectController {

    private final AstronomicalObjectService astronomicalObjectService;
    private final ModelMapper modelMapper;

    @Autowired
    public AstronomicalObjectController(AstronomicalObjectService astronomicalObjectService, ModelMapper modelMapper) {
        this.astronomicalObjectService = astronomicalObjectService;
        this.modelMapper = modelMapper;
    }

    // GET /api/astronomical-objects - получить все объекты
    @GetMapping
    public ResponseEntity<ApiResponse<List<AstronomicalObjectDTO>>> getAllObjects() {
        try {
            List<AstronomicalObjectDTO> objects = astronomicalObjectService.getAllObjects().stream()
                    .map(modelMapper::toAstronomicalObjectDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Astronomical objects retrieved successfully", objects));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving astronomical objects: " + e.getMessage()));
        }
    }

    // GET /api/astronomical-objects/{id} - получить объект по ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AstronomicalObjectDTO>> getObjectById(@PathVariable Long id) {
        try {
            AstronomicalObject object = astronomicalObjectService.getObjectById(id)
                    .orElseThrow(() -> new RuntimeException("Astronomical object not found with id: " + id));
            return ResponseEntity.ok(ApiResponse.success("Astronomical object retrieved successfully",
                    modelMapper.toAstronomicalObjectDTO(object)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving astronomical object: " + e.getMessage()));
        }
    }

    // POST /api/astronomical-objects - создать новый объект
    @PostMapping
    public ResponseEntity<ApiResponse<AstronomicalObjectDTO>> createObject(@RequestBody AstronomicalObjectDTO objectDTO) {
        try {
            AstronomicalObject object = modelMapper.toAstronomicalObjectEntity(objectDTO);

            AstronomicalObject createdObject = astronomicalObjectService.createObject(object);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Astronomical object created successfully",
                            modelMapper.toAstronomicalObjectDTO(createdObject)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error creating astronomical object: " + e.getMessage()));
        }
    }

    // PUT /api/astronomical-objects/{id} - обновить объект
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AstronomicalObjectDTO>> updateObject(@PathVariable Long id, @RequestBody AstronomicalObjectDTO objectDTO) {
        try {
            AstronomicalObject object = modelMapper.toAstronomicalObjectEntity(objectDTO);

            AstronomicalObject updatedObject = astronomicalObjectService.updateObject(id, object);
            return ResponseEntity.ok(ApiResponse.success("Astronomical object updated successfully",
                    modelMapper.toAstronomicalObjectDTO(updatedObject)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error updating astronomical object: " + e.getMessage()));
        }
    }

    // DELETE /api/astronomical-objects/{id} - удалить объект
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteObject(@PathVariable Long id) {
        try {
            astronomicalObjectService.deleteObject(id);
            return ResponseEntity.ok(ApiResponse.success("Astronomical object deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error deleting astronomical object: " + e.getMessage()));
        }
    }

    // GET /api/astronomical-objects/search?name={name} - поиск объектов по названию
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AstronomicalObjectDTO>>> searchObjectsByName(@RequestParam String name) {
        try {
            List<AstronomicalObjectDTO> objects = astronomicalObjectService.searchObjectsByName(name).stream()
                    .map(modelMapper::toAstronomicalObjectDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Objects found successfully", objects));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error searching objects: " + e.getMessage()));
        }
    }

    // GET /api/astronomical-objects/type/{type} - получить объекты по типу
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<AstronomicalObjectDTO>>> getObjectsByType(@PathVariable AstronomicalObjectType type) {
        try {
            List<AstronomicalObjectDTO> objects = astronomicalObjectService.getObjectsByType(type).stream()
                    .map(modelMapper::toAstronomicalObjectDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Objects retrieved successfully", objects));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving objects: " + e.getMessage()));
        }
    }

    // GET /api/astronomical-objects/constellation/{constellation} - получить объекты по созвездию
    @GetMapping("/constellation/{constellation}")
    public ResponseEntity<ApiResponse<List<AstronomicalObjectDTO>>> getObjectsByConstellation(@PathVariable String constellation) {
        try {
            List<AstronomicalObjectDTO> objects = astronomicalObjectService.getObjectsByConstellation(constellation).stream()
                    .map(modelMapper::toAstronomicalObjectDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Objects retrieved successfully", objects));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving objects: " + e.getMessage()));
        }
    }

    // GET /api/astronomical-objects/bright/{maxMagnitude} - получить яркие объекты
    @GetMapping("/bright/{maxMagnitude}")
    public ResponseEntity<ApiResponse<List<AstronomicalObjectDTO>>> getBrightObjects(@PathVariable Double maxMagnitude) {
        try {
            List<AstronomicalObjectDTO> objects = astronomicalObjectService.getBrightObjects(maxMagnitude).stream()
                    .map(modelMapper::toAstronomicalObjectDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Bright objects retrieved successfully", objects));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving bright objects: " + e.getMessage()));
        }
    }

    // GET /api/astronomical-objects/distance-range - объекты в диапазоне расстояний
    @GetMapping("/distance-range")
    public ResponseEntity<ApiResponse<List<AstronomicalObjectDTO>>> getObjectsInDistanceRange(
            @RequestParam Double minDistance,
            @RequestParam Double maxDistance) {
        try {
            List<AstronomicalObjectDTO> objects = astronomicalObjectService.getObjectsInDistanceRange(minDistance, maxDistance).stream()
                    .map(modelMapper::toAstronomicalObjectDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Objects in distance range retrieved successfully", objects));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving objects: " + e.getMessage()));
        }
    }

    // GET /api/astronomical-objects/popular - популярные объекты
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<AstronomicalObjectDTO>>> getPopularObjects() {
        try {
            List<AstronomicalObjectDTO> objects = astronomicalObjectService.getPopularObjects().stream()
                    .map(modelMapper::toAstronomicalObjectDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Popular objects retrieved successfully", objects));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving popular objects: " + e.getMessage()));
        }
    }
}