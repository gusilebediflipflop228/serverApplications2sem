package com.ashaev.serverapps2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Стандартизированный ответ об ошибке")
public class ApiErrorResponse {

    @Schema(description = "Статус успеха операции", example = "false")
    private boolean success;

    @Schema(description = "Код ошибки (аналог HTTP статуса)", example = "400")
    private int errorCode;

    @Schema(description = "Краткое сообщение об ошибке", example = "Ошибка валидации")
    private String errorMessage;

    @Schema(description = "Детальное описание проблем (например, ошибки полей)", example = "['name: Название не должно быть пустым']")
    private List<String> details;

    public static ApiErrorResponse error(int errorCode, String errorMessage, List<String> details) {
        return new ApiErrorResponse(false, errorCode, errorMessage, details);
    }
}