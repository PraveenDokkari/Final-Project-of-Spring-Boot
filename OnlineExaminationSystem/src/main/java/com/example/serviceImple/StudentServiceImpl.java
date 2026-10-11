package com.example.serviceImple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.StudentRequestDto;
import com.example.dto.StudentResponseDto;
import com.example.entities.Student;
import com.example.exceptions.BadRequestException;
import com.example.exceptions.ResourceNotFoundException;
import com.example.mapper.EntityMapper;
import com.example.repository.StudentRepository;
import com.example.service.StudentService;


import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{
	
	//Logger
	private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);
	
	
	private final StudentRepository studentRepository;
    private final EntityMapper mapper;
	
	@Transactional
	@Override
	public StudentResponseDto createStudent(StudentRequestDto dto) {
		
		    log.info("Creating student with email {}", dto.getEmail());
		    
	        if (studentRepository.existsByEmail(dto.getEmail())) {
	        	
	            throw new BadRequestException("Student already exists with email " + dto.getEmail());
	         }
	        
	        Student saved = studentRepository.save(mapper.toStudent(dto));
	        log.debug("Student created with id {}", saved.getId());
	        
	        return mapper.toStudentDto(saved);
	}
	
	//Customized method for updateStudent method
	private Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
    }
    
	@Transactional
	@Override
	public StudentResponseDto updateStudent(Long id, StudentRequestDto dto) {
		
	       log.info("Updating student {}", id);
	        Student student = findStudent(id);
	        student.setName(dto.getName());
	        student.setEmail(dto.getEmail());
	        return mapper.toStudentDto(studentRepository.save(student));
	}
	@Transactional(readOnly = true)
	@Override
	public StudentResponseDto getStudentById(Long id) {
	
		
        log.info("Fetching student {}", id);
        return mapper.toStudentDto(findStudent(id));
	}

}
