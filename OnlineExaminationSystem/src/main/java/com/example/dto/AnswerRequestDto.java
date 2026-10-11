package com.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class AnswerRequestDto {

    @NotNull(message = "Question id is required")
    private Long questionId;

    @NotNull(message = "Selected option id is required")
    private Long optionId;
}
