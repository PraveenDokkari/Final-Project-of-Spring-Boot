package com.example.service;

import java.util.List;

import com.example.dto.QuestionRequestDto;
import com.example.dto.QuestionResponseDto;

public interface QuestionService {
	
    QuestionResponseDto addQuestion(Long examId, QuestionRequestDto dto);

    QuestionResponseDto updateQuestion(Long questionId, QuestionRequestDto dto);

    List<QuestionResponseDto> getQuestionsByExam(Long examId);

}
