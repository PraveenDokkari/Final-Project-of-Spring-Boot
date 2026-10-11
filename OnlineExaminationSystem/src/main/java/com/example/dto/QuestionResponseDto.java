package com.example.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class QuestionResponseDto {

	    private Long id;
	    private String questionText;
	    private Integer marks;
	    private List<OptionResponseDto> options;
}
