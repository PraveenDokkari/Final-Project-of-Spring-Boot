package com.example.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.ResultResponseDto;
import com.example.service.ResultService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
public class ResultController {
	
	 private final ResultService resultService;

	    @Operation(summary = "Calculate result of a submitted attempt")
	    @PostMapping("/calculate/{attemptId}")
	    public ResponseEntity<ResultResponseDto> calculateResult(@PathVariable Long attemptId) {
	        return new ResponseEntity<>(resultService.calculateResult(attemptId), HttpStatus.CREATED);
	    }

	    @Operation(summary = "Get result of an attempt")
	    @GetMapping("/{attemptId}")
	    public ResponseEntity<ResultResponseDto> getResult(@PathVariable Long attemptId) {
	        return ResponseEntity.ok(resultService.getResultByAttempt(attemptId));
	    }
	    
	    @Operation(summary = "Rankings of an exam (highest score first)")
	    @GetMapping("/rankings/{examId}")
	    public ResponseEntity<Page<ResultResponseDto>> getRankings(
	            @PathVariable Long examId,
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size) {
	        return ResponseEntity.ok(resultService.getRankings(examId, page, size));
	    }

	    @Operation(summary = "Count results by status PASS / FAIL (native query)")
	    @GetMapping("/count/{status}")
	    public ResponseEntity<Long> countByStatus(@PathVariable String status) {
	        return ResponseEntity.ok(resultService.countByStatus(status));
	    }

}
