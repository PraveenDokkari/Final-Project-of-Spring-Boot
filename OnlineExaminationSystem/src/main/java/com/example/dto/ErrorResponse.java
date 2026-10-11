package com.example.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ErrorResponse {
	
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private Map<String, String> errors;

}
