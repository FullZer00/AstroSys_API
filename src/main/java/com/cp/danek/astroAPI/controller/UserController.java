package com.cp.danek.astroAPI.controller;

import com.cp.danek.astroAPI.dto.ApiResponse;
import com.cp.danek.astroAPI.dto.CreateUserDTO;
import com.cp.danek.astroAPI.dto.UserDTO;
import com.cp.danek.astroAPI.mapper.ModelMapper;
import com.cp.danek.astroAPI.model.entities.Role;
import com.cp.danek.astroAPI.model.entities.User;
import com.cp.danek.astroAPI.model.enums.UserRole;
import com.cp.danek.astroAPI.model.repositories.RoleRepository;
import com.cp.danek.astroAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, RoleRepository roleRepository, ModelMapper modelMapper) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    // GET /api/users - получить всех пользователей
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers().stream()
                    .map(modelMapper::toUserDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving users: " + e.getMessage()));
        }
    }

    // GET /api/users/{id} - получить пользователя по ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
            return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", modelMapper.toUserDTO(user)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving user: " + e.getMessage()));
        }
    }

    // GET /api/users/login/{login} - получить пользователя по логину
    @GetMapping("/login/{login}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByLogin(@PathVariable String login) {
        try {
            User user = userService.getUserByLogin(login)
                    .orElseThrow(() -> new RuntimeException("User not found with login: " + login));
            return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", modelMapper.toUserDTO(user)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving user: " + e.getMessage()));
        }
    }

    // POST /api/users - создать нового пользователя
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody CreateUserDTO request) {
        try {
            // Находим роль
            Role role = roleRepository.findByName(request.getRole())
                    .orElseThrow(() -> new RuntimeException("Role not found: " + request.getRole()));

            // Создаем пользователя через маппер
            User user = modelMapper.toUserEntity(request);
            user.setRole(role);

            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("User created successfully", modelMapper.toUserDTO(createdUser)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error creating user: " + e.getMessage()));
        }
    }

    // PUT /api/users/{id} - обновить пользователя
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Long id, @RequestBody CreateUserDTO request) {
        try {
            // Находим существующего пользователя
            User existingUser = userService.getUserById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

            // Находим роль
            Role role = roleRepository.findByName(request.getRole())
                    .orElseThrow(() -> new RuntimeException("Role not found: " + request.getRole()));

            // Обновляем данные через маппер
            modelMapper.updateUserFromRequest(existingUser, request);
            existingUser.setRole(role);

            User updatedUser = userService.updateUser(id, existingUser);
            return ResponseEntity.ok(ApiResponse.success("User updated successfully", modelMapper.toUserDTO(updatedUser)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error updating user: " + e.getMessage()));
        }
    }

    // DELETE /api/users/{id} - удалить пользователя
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error deleting user: " + e.getMessage()));
        }
    }

    // PATCH /api/users/{id}/deactivate - деактивировать пользователя
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<UserDTO>> deactivateUser(@PathVariable Long id) {
        try {
            userService.deactivateUser(id);
            User user = userService.getUserById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
            return ResponseEntity.ok(ApiResponse.success("User deactivated successfully", modelMapper.toUserDTO(user)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error deactivating user: " + e.getMessage()));
        }
    }

    // GET /api/users/role/{role} - получить пользователей по роли
    @GetMapping("/role/{role}")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getUsersByRole(@PathVariable UserRole role) {
        try {
            List<UserDTO> users = userService.getUsersByRole(role).stream()
                    .map(modelMapper::toUserDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving users: " + e.getMessage()));
        }
    }

    // GET /api/users/astronomers/active - получить активных астрономов
    @GetMapping("/astronomers/active")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getActiveAstronomers() {
        try {
            List<UserDTO> astronomers = userService.getActiveAstronomers().stream()
                    .map(modelMapper::toUserDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Active astronomers retrieved successfully", astronomers));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving astronomers: " + e.getMessage()));
        }
    }

    // GET /api/users/engineers/active - получить активных инженеров
    @GetMapping("/engineers/active")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getActiveEngineers() {
        try {
            List<UserDTO> engineers = userService.getActiveEngineers().stream()
                    .map(modelMapper::toUserDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Active engineers retrieved successfully", engineers));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving engineers: " + e.getMessage()));
        }
    }
}