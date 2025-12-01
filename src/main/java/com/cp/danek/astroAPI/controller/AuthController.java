package com.cp.danek.astroAPI.controller;

import com.cp.danek.astroAPI.dto.*;
import com.cp.danek.astroAPI.mapper.ModelMapper;
import com.cp.danek.astroAPI.security.UserDetailsImpl;
import com.cp.danek.astroAPI.service.AuthService;
import com.cp.danek.astroAPI.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Аутентификация", description = "API для аутентификации и регистрации")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Регистрация пользователя",
            description = "Выполняет регистрацию пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешная регистрация",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    @PostMapping("/reg")
    public ResponseEntity<ApiResponseDTO<UserDTO>> register(@Valid @RequestBody RegistrationRequestDTO regRequestDTO) {
        try {
            UserDTO createdUser = authService.register(regRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.success("User registration successfully",createdUser));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Registration error: " + e.getMessage()));
        }
    }

    @Operation(
            summary = "Аутентификация пользователя",
            description = "Выполняет вход пользователя и возвращает JWT токен"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная аутентификация",
                    content = @Content(schema = @Schema(implementation = JwtResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<JwtResponseDTO>> authenticateUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Учетные данные пользователя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequestDTO.class))
            )
            @Valid @RequestBody LoginRequestDTO loginRequest) {

        try {
            JwtResponseDTO jwtResponse = authService.authenticate(loginRequest);

            return ResponseEntity.ok(ApiResponseDTO.success("User authenticated successfully", jwtResponse));

        } catch (org.springframework.security.core.AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDTO.error("Invalid login or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Authentication error: " + e.getMessage()));
        }
    }

    @Operation(
            summary = "Проверка токена",
            description = "Проверяет валидность JWT токена и возвращает информацию о пользователе"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Токен валиден"),
            @ApiResponse(responseCode = "401", description = "Токен невалиден или истек")
    })
    @GetMapping("/validate")
    public ResponseEntity<ApiResponseDTO<JwtResponseDTO>> validateToken() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponseDTO.error("Invalid or expired token"));
            }

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String role = userDetails.getAuthorities().iterator().next().getAuthority()
                    .replace("ROLE_", "");

            JwtResponseDTO jwtResponse = new JwtResponseDTO(
                    "valid", // В реальном приложении можно вернуть новый токен
                    userDetails.getId(),
                    userDetails.getUsername(),
                    role
            );

            return ResponseEntity.ok(ApiResponseDTO.success("Token is valid", jwtResponse));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDTO.error("Invalid or expired token"));
        }
    }

    @Operation(
            summary = "Выход пользователя",
            description = "Выполняет выход пользователя из системы"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный выход"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDTO<Void>> logoutUser() {
        try {
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok(ApiResponseDTO.success("User logged out successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Logout error: " + e.getMessage()));
        }
    }
}