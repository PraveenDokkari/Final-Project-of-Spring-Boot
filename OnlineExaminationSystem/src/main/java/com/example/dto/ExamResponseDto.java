package com.example.dto;

import com.example.enums.ExamStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ExamResponseDto {
	
	private Long id;
    private String title;
    private String description;
    private Integer durationMinutes;
    private Integer passMarks;
    private ExamStatus status;

}
