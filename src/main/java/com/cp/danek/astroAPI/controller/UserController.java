package com.cp.danek.astroAPI.controller;

import com.cp.danek.astroAPI.dto.ApiResponseDTO;
import com.cp.danek.astroAPI.dto.CreateUserDTO;
import com.cp.danek.astroAPI.dto.UserDTO;
import com.cp.danek.astroAPI.mapper.ModelMapper;
import com.cp.danek.astroAPI.model.entities.Role;
import com.cp.danek.astroAPI.model.entities.User;
import com.cp.danek.astroAPI.model.enums.UserRole;
import com.cp.danek.astroAPI.model.repositories.RoleRepository;
import com.cp.danek.astroAPI.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Пользователи", description = "API для управления пользователями системы")
@ApiResponse(responseCode = "403", description = "Ошибка аутентификации")
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
    @Operation(
            summary = "Получить всех пользователей",
            description = "Возвращает список всех пользователей системы с пагинацией"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка пользователей",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<UserDTO>>> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers().stream()
                    .map(modelMapper::toUserDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Users retrieved successfully", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving users: " + e.getMessage()));
        }
    }

    // GET /api/users/{id} - получить пользователя по ID
    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает информацию о пользователе по его идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserDTO>> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
            return ResponseEntity.ok(ApiResponseDTO.success("User retrieved successfully", modelMapper.toUserDTO(user)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving user: " + e.getMessage()));
        }
    }

    // GET /api/users/login/{login} - получить пользователя по логину
    @Operation(
            summary = "Получить пользователя по логину",
            description = "Возвращает информацию о пользователе по его логину."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь с указанным логином не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('ADMIN') or #login == authentication.principal.login")
    @GetMapping("/login/{login}")
    public ResponseEntity<ApiResponseDTO<UserDTO>> getUserByLogin(@PathVariable String login) {
        try {
            User user = userService.getUserByLogin(login)
                    .orElseThrow(() -> new RuntimeException("User not found with login: " + login));
            return ResponseEntity.ok(ApiResponseDTO.success("User retrieved successfully", modelMapper.toUserDTO(user)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving user: " + e.getMessage()));
        }
    }

    // POST /api/users - создать нового пользователя
    @Operation(
            summary = "Создать нового пользователя",
            description = "Создает нового пользователя в системе. Требуются права администратора."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<UserDTO>> createUser(@RequestBody CreateUserDTO request) {
        try {
            // Находим роль
            Role role = roleRepository.findByName(request.getRole())
                    .orElseThrow(() -> new RuntimeException("Role not found: " + request.getRole()));

            // Создаем пользователя через маппер
            User user = modelMapper.toUserEntity(request);
            user.setRole(role);

            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.success("User created successfully", modelMapper.toUserDTO(createdUser)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error creating user: " + e.getMessage()));
        }
    }

    // PUT /api/users/{id} - обновить пользователя
    @Operation(
            summary = "Изменить пользователя",
            description = "Изменяет пользователя в системе. Требуются права администратора."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно изменен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserDTO>> updateUser(@PathVariable Long id, @RequestBody CreateUserDTO request) {
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
            return ResponseEntity.ok(ApiResponseDTO.success("User updated successfully", modelMapper.toUserDTO(updatedUser)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error updating user: " + e.getMessage()));
        }
    }

    // DELETE /api/users/{id} - удалить пользователя
    @Operation(
            summary = "Удалить пользователя по id",
            description = "Удаляет пользователя в системе. Требуются права администратора."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponseDTO.success("User deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error deleting user: " + e.getMessage()));
        }
    }

    // PATCH /api/users/{id}/deactivate - деактивировать пользователя
    @Operation(
            summary = "Деактивировать пользователя по id",
            description = "Деактивирует пользователя в системе. Требуются права администратора."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно дективирован"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponseDTO<UserDTO>> deactivateUser(@PathVariable Long id) {
        try {
            userService.deactivateUser(id);
            User user = userService.getUserById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
            return ResponseEntity.ok(ApiResponseDTO.success("User deactivated successfully", modelMapper.toUserDTO(user)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error deactivating user: " + e.getMessage()));
        }
    }

    // GET /api/users/role/{role} - получить пользователей по роли
    @Operation(
            summary = "Получить пользователей по роли",
            description = "Возвращает информацию о пользователях по их ролям"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователи успешно найдены"),
            @ApiResponse(responseCode = "404", description = "Пользователи не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/role/{role}")
    public ResponseEntity<ApiResponseDTO<List<UserDTO>>> getUsersByRole(@PathVariable UserRole role) {
        try {
            List<UserDTO> users = userService.getUsersByRole(role).stream()
                    .map(modelMapper::toUserDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Users retrieved successfully", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving users: " + e.getMessage()));
        }
    }

    // GET /api/users/astronomers/active - получить активных астрономов
    @Operation(
            summary = "Получить активных пользователей с ролью Астроном",
            description = "Возвращает информацию о активных пользователях с ролью Астроном"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователи успешно найдены"),
            @ApiResponse(responseCode = "404", description = "Пользователи не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/astronomers/active")
    public ResponseEntity<ApiResponseDTO<List<UserDTO>>> getActiveAstronomers() {
        try {
            List<UserDTO> astronomers = userService.getActiveAstronomers().stream()
                    .map(modelMapper::toUserDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Active astronomers retrieved successfully", astronomers));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving astronomers: " + e.getMessage()));
        }
    }

    // GET /api/users/engineers/active - получить активных инженеров
    @Operation(
            summary = "Получить активных пользователей с ролью Инеженер",
            description = "Возвращает информацию о активных пользователях с ролью Инеженер"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователи успешно найдены"),
            @ApiResponse(responseCode = "404", description = "Пользователи не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/engineers/active")
    public ResponseEntity<ApiResponseDTO<List<UserDTO>>> getActiveEngineers() {
        try {
            List<UserDTO> engineers = userService.getActiveEngineers().stream()
                    .map(modelMapper::toUserDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponseDTO.success("Active engineers retrieved successfully", engineers));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error retrieving engineers: " + e.getMessage()));
        }
    }
}