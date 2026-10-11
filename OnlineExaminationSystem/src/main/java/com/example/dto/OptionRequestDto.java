package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class OptionRequestDto {

	   @NotBlank(message = "Option text is required")
	    private String optionText;

	    private boolean correct;
	
}
