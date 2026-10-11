package com.example.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class SubmitAnswersRequestDto {
	
    @Valid
    @NotEmpty(message = "At least one answer is required")
    private List<AnswerRequestDto> answers;

}
