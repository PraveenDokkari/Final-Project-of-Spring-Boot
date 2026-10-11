package com.example.dto;

import com.example.enums.ResultStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ResultResponseDto {

	
    private Long attemptId;
    private String studentName;
    private String examTitle;
    private Integer score;
    private Integer totalMarks;
    private Double percentage;
    private ResultStatus status;
}
