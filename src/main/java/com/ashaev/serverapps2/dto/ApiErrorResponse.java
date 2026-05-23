package com.ashaev.serverapps2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
    private boolean success;
    private String errorCode;
    private String errorMessage;
    private List<String> details;

    public static ApiErrorResponse error(String errorCode, String errorMessage, List<String> details) {
        return new ApiErrorResponse(false, errorCode, errorMessage, details);
    }
}