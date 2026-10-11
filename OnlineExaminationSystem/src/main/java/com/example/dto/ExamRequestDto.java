package com.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ExamRequestDto {
	
	 @NotBlank(message = "Title is required")
	    private String title;

	    private String description;

	    @NotNull(message = "Duration is required")
	    @Min(value = 1, message = "Duration must be at least 1 minute")
	    private Integer durationMinutes;

	    @NotNull(message = "Pass marks is required")
	    @Min(value = 0, message = "Pass marks cannot be negative")
	    private Integer passMarks;

}
