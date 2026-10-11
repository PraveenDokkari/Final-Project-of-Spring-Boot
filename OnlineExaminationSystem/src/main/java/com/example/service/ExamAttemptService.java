package com.example.service;

import com.example.dto.AttemptResponseDto;
import com.example.dto.StartExamRequestDto;
import com.example.dto.SubmitAnswersRequestDto;

public interface ExamAttemptService {
	
    AttemptResponseDto startExam(StartExamRequestDto dto);

    AttemptResponseDto submitAnswers(Long attemptId, SubmitAnswersRequestDto dto);

    int autoSubmitExpiredAttempts();

}
