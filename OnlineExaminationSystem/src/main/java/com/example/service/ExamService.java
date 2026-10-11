package com.example.service;

import org.springframework.data.domain.Page;

import com.example.dto.ExamRequestDto;
import com.example.dto.ExamResponseDto;

public interface ExamService {
	
	
    ExamResponseDto createExam(ExamRequestDto dto);

    ExamResponseDto updateExam(Long id, ExamRequestDto dto);

    ExamResponseDto getExamById(Long id);

    Page<ExamResponseDto> getAllExams(int page, int size, String sortBy, String direction);

    Page<ExamResponseDto> getPublishedExams(int page, int size);

    ExamResponseDto publishExam(Long id);

}
