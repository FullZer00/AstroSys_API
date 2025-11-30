package com.cp.danek.astroAPI.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Стандартный ответ API")
public class ApiResponseDTO<T> {

    @Schema(description = "Флаг успешности операции", example = "true")
    private boolean success;

    @Schema(description = "Сообщение для пользователя", example = "Operation completed successfully")
    private String message;

    @Schema(description = "Данные ответа")
    private T data;

    @Schema(description = "Сообщение об ошибке", example = "Resource not found")
    private String error;

    // Конструкторы
    public ApiResponseDTO() {}

    public ApiResponseDTO(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ApiResponseDTO(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    // Статические методы для удобства
    public static <T> ApiResponseDTO<T> success(T data) {
        return new ApiResponseDTO<>(true, "Operation completed successfully", data);
    }

    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return new ApiResponseDTO<>(true, message, data);
    }

    public static <T> ApiResponseDTO<T> error(String error) {
        return new ApiResponseDTO<>(false, error);
    }

    // Геттеры и сеттеры
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}