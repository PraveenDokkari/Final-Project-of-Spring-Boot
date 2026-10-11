package com.example.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.ExamRequestDto;
import com.example.dto.ExamResponseDto;
import com.example.service.ExamService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {
	
	 private final ExamService examService;
	 
	    @Operation(summary = "Create an exam ")
	    @PostMapping
	    public ResponseEntity<ExamResponseDto> createExam(@Valid @RequestBody ExamRequestDto dto) {
	    	
	        return new ResponseEntity<>(examService.createExam(dto), HttpStatus.CREATED);
	    }

	    @Operation(summary = "Update an exam ")
	    @PutMapping("/{id}")
	    public ResponseEntity<ExamResponseDto> updateExam(@PathVariable Long id,
	                                                      @Valid @RequestBody ExamRequestDto dto) {
	        return ResponseEntity.ok(examService.updateExam(id, dto));
	    }

	    @Operation(summary = "Publish an exam so students can start it ")
	    @PatchMapping("/{id}/publish")
	    public ResponseEntity<ExamResponseDto> publishExam(@PathVariable Long id) {
	    	
	        return ResponseEntity.ok(examService.publishExam(id));
	    }
	    
	    @Operation(summary = "Get an exam by id")
	    @GetMapping("/{id}")
	    public ResponseEntity<ExamResponseDto> getExam(@PathVariable Long id) {
	    	
	        return ResponseEntity.ok(examService.getExamById(id));
	    }

	    @Operation(summary = "Get all exams with pagination and sorting")
	    @GetMapping
	    public ResponseEntity<Page<ExamResponseDto>> getAllExams(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "5") int size,
	            @RequestParam(defaultValue = "id") String sortBy,
	            @RequestParam(defaultValue = "asc") String direction) {
	    	
	        return ResponseEntity.ok(examService.getAllExams(page, size, sortBy, direction));
	    }

	    @Operation(summary = "Get published exams (JPQL query)")
	    @GetMapping("/published")
	    public ResponseEntity<Page<ExamResponseDto>> getPublishedExams(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "5") int size) {
	    	
	        return ResponseEntity.ok(examService.getPublishedExams(page, size));
	    }

}
