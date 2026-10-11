package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.dto.ExamRequestDto;
import com.example.dto.ExamResponseDto;
import com.example.entities.Exam;
import com.example.enums.ExamStatus;
import com.example.exceptions.BadRequestException;
import com.example.exceptions.ResourceNotFoundException;
import com.example.mapper.EntityMapper;
import com.example.repository.ExamRepository;
import com.example.serviceImple.ExamServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ExamServiceImplTest {
	
	
	   @Mock
	    private ExamRepository examRepository;

	    @Spy
	    private EntityMapper mapper = new EntityMapper();

	    @InjectMocks
	    private ExamServiceImpl examService;

	    @Test
	    void createExam_shouldSaveAsDraft() {
	        ExamRequestDto dto = new ExamRequestDto();
	        dto.setTitle("Java Basics");
	        dto.setDescription("Core java");
	        dto.setDurationMinutes(30);
	        dto.setPassMarks(10);
	        
	        when(examRepository.save(any(Exam.class))).thenAnswer(invocation -> {
	            Exam exam = invocation.getArgument(0);
	            exam.setId(1L);
	            return exam;
	        });

	        ExamResponseDto response = examService.createExam(dto);

	        assertEquals(1L, response.getId());
	        assertEquals("Java Basics", response.getTitle());
	        assertEquals(ExamStatus.DRAFT, response.getStatus());
	    }

	    @Test
	    void getExamById_whenNotFound_shouldThrowException() {
	        when(examRepository.findById(99L)).thenReturn(Optional.empty());

	        assertThrows(ResourceNotFoundException.class, () -> examService.getExamById(99L));
	    }
	    
	    @Test
	    void publishExam_withoutQuestions_shouldThrowException() {
	        Exam exam = new Exam();
	        exam.setId(1L);
	        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));

	        assertThrows(BadRequestException.class, () -> examService.publishExam(1L));
	    }

}
