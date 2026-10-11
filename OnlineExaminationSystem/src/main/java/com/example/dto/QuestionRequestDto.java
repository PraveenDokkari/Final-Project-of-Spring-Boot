package com.example.dto;



import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class QuestionRequestDto {

	
	@NotBlank(message = "Question text is required")
    private String questionText;

    @NotNull(message = "Marks is required")
    @Min(value = 1, message = "Marks must be at least 1")
    private Integer marks;

    @Valid
    @NotNull(message = "Options are required")
    @Size(min = 2, message = "A question needs at least 2 options")
    private List<OptionRequestDto> options;
}
