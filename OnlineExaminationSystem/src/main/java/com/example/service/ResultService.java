package com.example.service;

import org.springframework.data.domain.Page;

import com.example.dto.ResultResponseDto;

public interface ResultService {
	
    ResultResponseDto calculateResult(Long attemptId);

    ResultResponseDto getResultByAttempt(Long attemptId);

    Page<ResultResponseDto> getRankings(Long examId, int page, int size);

    long countByStatus(String status);

}
