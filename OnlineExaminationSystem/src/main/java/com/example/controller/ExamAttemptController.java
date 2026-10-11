package com.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.AttemptResponseDto;
import com.example.dto.StartExamRequestDto;
import com.example.dto.SubmitAnswersRequestDto;
import com.example.service.ExamAttemptService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/attempts")
@RequiredArgsConstructor
public class ExamAttemptController {
	
	private final ExamAttemptService attemptService;

    @Operation(summary = "Start an exam")
    @PostMapping("/start")
    public ResponseEntity<AttemptResponseDto> startExam(@Valid @RequestBody StartExamRequestDto dto) {
    	
        return new ResponseEntity<>(attemptService.startExam(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Submit answers of an attempt")
    @PostMapping("/{attemptId}/submit")
    public ResponseEntity<AttemptResponseDto> submitAnswers(@PathVariable Long attemptId,
                                                            @Valid @RequestBody SubmitAnswersRequestDto dto) {
    	
        return ResponseEntity.ok(attemptService.submitAnswers(attemptId, dto));
    }

}
