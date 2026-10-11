package com.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class StartExamRequestDto {

	
	    @NotNull(message = "Student id is required")
	    private Long studentId;

	    @NotNull(message = "Exam id is required")
	    private Long examId;
}
