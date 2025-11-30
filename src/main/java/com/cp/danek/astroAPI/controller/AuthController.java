package com.cp.danek.astroAPI.controller;

import com.cp.danek.astroAPI.dto.ApiResponseDTO;
import com.cp.danek.astroAPI.dto.LoginRequestDTO;
import com.cp.danek.astroAPI.dto.JwtResponseDTO;
import com.cp.danek.astroAPI.security.JwtTokenProvider;
import com.cp.danek.astroAPI.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Аутентификация", description = "API для аутентификации и получения JWT токенов")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
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
            // Аутентификация пользователя
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getLogin(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Генерация JWT токена
            String jwt = jwtTokenProvider.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String role = userDetails.getAuthorities().iterator().next().getAuthority()
                    .replace("ROLE_", ""); // Убираем префикс ROLE_

            JwtResponseDTO jwtResponse = new JwtResponseDTO(
                    jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    role
            );

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