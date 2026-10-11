package com.example.dto;

import java.time.LocalDateTime;

import com.example.enums.AttemptStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class AttemptResponseDto {

	
    private Long attemptId;
    private Long studentId;
    private Long examId;
    private LocalDateTime startTime;
    private LocalDateTime submitTime;
    private AttemptStatus status;
}
