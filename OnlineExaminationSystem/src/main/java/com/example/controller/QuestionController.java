package com.example.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.QuestionRequestDto;
import com.example.dto.QuestionResponseDto;
import com.example.service.QuestionService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuestionController {
	

    private final QuestionService questionService;

    @Operation(summary = "Add a question with options to an exam (ADMIN)")
    @PostMapping("/exams/{examId}/questions")
    public ResponseEntity<QuestionResponseDto> addQuestion(@PathVariable Long examId,
                                                           @Valid @RequestBody QuestionRequestDto dto) {
    	
        return new ResponseEntity<>(questionService.addQuestion(examId, dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a question (ADMIN)")
    @PutMapping("/questions/{questionId}")
    public ResponseEntity<QuestionResponseDto> updateQuestion(@PathVariable Long questionId,
                                                              @Valid @RequestBody QuestionRequestDto dto) {
        
    	    return ResponseEntity.ok(questionService.updateQuestion(questionId, dto));
    }

    @Operation(summary = "Get all questions of an exam (correct answers are hidden)")
    @GetMapping("/exams/{examId}/questions")
    public ResponseEntity<List<QuestionResponseDto>> getQuestions(@PathVariable Long examId) {
    	
        return ResponseEntity.ok(questionService.getQuestionsByExam(examId));
    }

}
