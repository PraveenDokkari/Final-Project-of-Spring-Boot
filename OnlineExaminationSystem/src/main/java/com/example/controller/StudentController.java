package com.example.controller;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.StudentRequestDto;
import com.example.dto.StudentResponseDto;
import com.example.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor

public class StudentController {
	
	 private final StudentService studentService;
	 
	    @Operation(summary = "Register a new student")
	    @PostMapping
	    public ResponseEntity<StudentResponseDto> createStudent(@Valid @RequestBody StudentRequestDto dto) {
	        return new ResponseEntity<>(studentService.createStudent(dto), HttpStatus.CREATED);
	    }

	    @Operation(summary = "Update a student")
	    @PutMapping("/{id}")
	    public ResponseEntity<StudentResponseDto> updateStudent(@PathVariable Long id,
	                                                            @Valid @RequestBody StudentRequestDto dto) {
	        return ResponseEntity.ok(studentService.updateStudent(id, dto));
	    }

	    @Operation(summary = "Get a student by id")
	    @GetMapping("/{id}")
	    public ResponseEntity<StudentResponseDto> getStudent(@PathVariable Long id) {
	        return ResponseEntity.ok(studentService.getStudentById(id));
	    }


}
