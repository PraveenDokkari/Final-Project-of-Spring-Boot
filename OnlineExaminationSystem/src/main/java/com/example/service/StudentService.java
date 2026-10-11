package com.example.service;

import com.example.dto.StudentRequestDto;
import com.example.dto.StudentResponseDto;

public interface StudentService {
	
    StudentResponseDto createStudent(StudentRequestDto dto);

    StudentResponseDto updateStudent(Long id, StudentRequestDto dto);

    StudentResponseDto getStudentById(Long id);

}
