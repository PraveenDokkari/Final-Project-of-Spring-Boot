package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.dto.ResultResponseDto;
import com.example.entities.Answer;
import com.example.entities.Exam;
import com.example.entities.ExamAttempt;
import com.example.entities.Option;
import com.example.entities.Question;
import com.example.entities.Result;
import com.example.entities.Student;
import com.example.enums.AttemptStatus;
import com.example.enums.ResultStatus;
import com.example.exceptions.BadRequestException;
import com.example.mapper.EntityMapper;
import com.example.repository.ExamAttemptRepository;
import com.example.repository.QuestionRepository;
import com.example.repository.ResultRepository;
import com.example.serviceImple.ResultServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ResultServiceImplTest {
	
	
	   @Mock
	    private ResultRepository resultRepository;

	    @Mock
	    private ExamAttemptRepository attemptRepository;

	    @Mock
	    private QuestionRepository questionRepository;

	    @Spy
	    private EntityMapper mapper = new EntityMapper();

	    @InjectMocks
	    private ResultServiceImpl resultService;
	    
	    @Test
	    void calculateResult_shouldGivePassWhenScoreIsEnough() {
	        Exam exam = new Exam();
	        exam.setId(1L);
	        exam.setTitle("Java");
	        exam.setPassMarks(5);

	        Student student = new Student();
	        student.setId(1L);
	        student.setName("Ravi");

	        ExamAttempt attempt = new ExamAttempt();
	        attempt.setId(1L);
	        attempt.setExam(exam);
	        attempt.setStudent(student);
	        attempt.setStatus(AttemptStatus.SUBMITTED);

	        Question q1 = new Question();
	        q1.setId(1L);
	        q1.setMarks(5);
	        Question q2 = new Question();
	        q2.setId(2L);
	        q2.setMarks(5);
	        
	        Option correctOption = new Option();
	        correctOption.setCorrect(true);
	        Option wrongOption = new Option();
	        wrongOption.setCorrect(false);

	        Answer a1 = new Answer();
	        a1.setQuestion(q1);
	        a1.setSelectedOption(correctOption);
	        Answer a2 = new Answer();
	        a2.setQuestion(q2);
	        a2.setSelectedOption(wrongOption);
	        attempt.getAnswers().add(a1);
	        attempt.getAnswers().add(a2);
	        
	        when(attemptRepository.findById(1L)).thenReturn(Optional.of(attempt));
	        when(resultRepository.existsByAttemptId(1L)).thenReturn(false);
	        when(questionRepository.findByExamId(1L)).thenReturn(List.of(q1, q2));
	        when(resultRepository.save(any(Result.class))).thenAnswer(invocation -> invocation.getArgument(0));

	        ResultResponseDto response = resultService.calculateResult(1L);

	        assertEquals(5, response.getScore());
	        assertEquals(10, response.getTotalMarks());
	        assertEquals(50.0, response.getPercentage());
	        assertEquals(ResultStatus.PASS, response.getStatus());
	    }
	    

	    @Test
	    void calculateResult_whenAttemptNotSubmitted_shouldThrowException() {
	        ExamAttempt attempt = new ExamAttempt();
	        attempt.setId(2L);
	        attempt.setStatus(AttemptStatus.STARTED);
	        when(attemptRepository.findById(2L)).thenReturn(Optional.of(attempt));

	        assertThrows(BadRequestException.class, () -> resultService.calculateResult(2L));
	    }

	    @Test
	    void countByStatus_withInvalidStatus_shouldThrowException() {
	        assertThrows(BadRequestException.class, () -> resultService.countByStatus("MAYBE"));
	    }

}
