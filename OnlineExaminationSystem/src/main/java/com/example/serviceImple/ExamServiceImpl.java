package com.example.serviceImple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.ExamRequestDto;
import com.example.dto.ExamResponseDto;
import com.example.entities.Exam;
import com.example.enums.ExamStatus;
import com.example.exceptions.BadRequestException;
import com.example.exceptions.ResourceNotFoundException;
import com.example.mapper.EntityMapper;
import com.example.repository.ExamRepository;
import com.example.service.ExamService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService{
	
	 private static final Logger log = LoggerFactory.getLogger(ExamServiceImpl.class);
	 
	    private final ExamRepository examRepository;
	    private final EntityMapper mapper;
	
	@Transactional
	@Override
	public ExamResponseDto createExam(ExamRequestDto dto) {
		
        log.info("Creating exam '{}'", dto.getTitle());
        
        Exam exam = mapper.toExam(dto);
        exam.setStatus(ExamStatus.DRAFT);
        
        Exam saved = examRepository.save(exam);
        log.debug("Exam created with id {}", saved.getId());
        
        
        return mapper.toExamDto(saved);
		
	}
	
	 private Exam findExam(Long id) {
	        return examRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id " + id));
	    }

	@Transactional
	@CacheEvict(value = "exams", key = "#id")
	@Override
	public ExamResponseDto updateExam(Long id, ExamRequestDto dto) {
		
		 log.info("Updating exam {}", id);
		 
	        Exam exam = findExam(id);
	        mapper.updateExam(exam, dto);
	        return mapper.toExamDto(examRepository.save(exam));
	}
  
	@Transactional(readOnly = true)
	@Cacheable(value = "exams", key = "#id")
	@Override
	public ExamResponseDto getExamById(Long id) {
		
		 log.info("Fetching exam {} from database", id);
		 
	        return mapper.toExamDto(findExam(id));
	}

	@Transactional(readOnly = true)
	@Override
	public Page<ExamResponseDto> getAllExams(int page, int size, String sortBy, String direction) {
		
		 log.info("Fetching exams page={}, size={}, sortBy={}, direction={}", page, size, sortBy, direction);
	        
		 Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
	        Pageable pageable = PageRequest.of(page, size, sort);
	        
	        return examRepository.findAll(pageable).map(mapper::toExamDto);
	}
    
	@Transactional(readOnly = true)
	@Override
	public Page<ExamResponseDto> getPublishedExams(int page, int size) {
		
		 log.info("Fetching published exams page={}, size={}", page, size);
		 
	        Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
	        
	        return examRepository.findByStatus(ExamStatus.PUBLISHED, pageable).map(mapper::toExamDto);
	}

	@Transactional
	@CacheEvict(value = "exams", key = "#id")
	@Override
	public ExamResponseDto publishExam(Long id) {
		
		 log.info("Publishing exam {}", id);
		 
	        Exam exam = findExam(id);
	        if (exam.getQuestions().isEmpty()) {
	            throw new BadRequestException("Cannot publish an exam without questions");
	        }
	        exam.setStatus(ExamStatus.PUBLISHED);
	        
	        return mapper.toExamDto(examRepository.save(exam));
	}

}
