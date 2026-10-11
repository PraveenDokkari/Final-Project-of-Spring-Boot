package com.example.serviceImple;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.AnswerRequestDto;
import com.example.dto.AttemptResponseDto;
import com.example.dto.StartExamRequestDto;
import com.example.dto.SubmitAnswersRequestDto;
import com.example.entities.Answer;
import com.example.entities.Exam;
import com.example.entities.ExamAttempt;
import com.example.entities.Option;
import com.example.entities.Question;
import com.example.entities.Student;
import com.example.enums.AttemptStatus;
import com.example.enums.ExamStatus;
import com.example.exceptions.BadRequestException;
import com.example.exceptions.ResourceNotFoundException;
import com.example.mapper.EntityMapper;
import com.example.repository.ExamAttemptRepository;
import com.example.repository.ExamRepository;
import com.example.repository.OptionRepository;
import com.example.repository.QuestionRepository;
import com.example.repository.StudentRepository;
import com.example.service.ExamAttemptService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExamAttemptServiceImpl implements ExamAttemptService {
	
	 private static final Logger log = LoggerFactory.getLogger(ExamAttemptServiceImpl.class);

	    private final ExamAttemptRepository attemptRepository;
	    private final StudentRepository studentRepository;
	    private final ExamRepository examRepository;
	    private final QuestionRepository questionRepository;
	    private final OptionRepository optionRepository;
	    private final EntityMapper mapper;
	
	
	@Transactional
	@Override
	public AttemptResponseDto startExam(StartExamRequestDto dto) {
		
		 log.info("Student {} is starting exam {}", dto.getStudentId(), dto.getExamId());

	        Student student = studentRepository.findById(dto.getStudentId())
	                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + dto.getStudentId()));
	        
	        Exam exam = examRepository.findById(dto.getExamId())
	                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id " + dto.getExamId()));

	        if (exam.getStatus() != ExamStatus.PUBLISHED) {
	            throw new BadRequestException("Exam is not published");
	        }
	        if (attemptRepository.existsByStudentIdAndExamId(student.getId(), exam.getId())) {
	            throw new BadRequestException("Student has already attempted this exam");
	        }

	        ExamAttempt attempt = new ExamAttempt();
	        attempt.setStudent(student);
	        attempt.setExam(exam);
	        attempt.setStartTime(LocalDateTime.now());
	        attempt.setStatus(AttemptStatus.STARTED);

	        ExamAttempt saved = attemptRepository.save(attempt);
	        log.debug("Attempt created with id {}", saved.getId());
	        
	        
	        return mapper.toAttemptDto(saved);
	}
    
	@Transactional
	@Override
	public AttemptResponseDto submitAnswers(Long attemptId, SubmitAnswersRequestDto dto) {
		
		  log.info("Submitting answers for attempt {}", attemptId);

	        ExamAttempt attempt = attemptRepository.findById(attemptId)
	                .orElseThrow(() -> new ResourceNotFoundException("Attempt not found with id " + attemptId));

	        if (attempt.getStatus() != AttemptStatus.STARTED) {
	            throw new BadRequestException("Attempt is already submitted");
	        }

	        Set<Long> answeredQuestions = new HashSet<>();
	        for (AnswerRequestDto answerDto : dto.getAnswers()) {
	        	
	            if (!answeredQuestions.add(answerDto.getQuestionId())) {
	                throw new BadRequestException("Question " + answerDto.getQuestionId() + " answered twice");
	            }

	            Question question = questionRepository.findById(answerDto.getQuestionId())
	                    .orElseThrow(() -> new ResourceNotFoundException(
	                            "Question not found with id " + answerDto.getQuestionId()));
	            
	            if (!question.getExam().getId().equals(attempt.getExam().getId())) {
	                throw new BadRequestException("Question " + question.getId() + " does not belong to this exam");
	            }

	            Option option = optionRepository.findById(answerDto.getOptionId())
	                    .orElseThrow(() -> new ResourceNotFoundException(
	                            "Option not found with id " + answerDto.getOptionId()));
	            
	            if (!option.getQuestion().getId().equals(question.getId())) {
	                throw new BadRequestException("Option " + option.getId() + " does not belong to question "
	                        + question.getId());
	            }

	            Answer answer = new Answer();
	            answer.setAttempt(attempt);
	            answer.setQuestion(question);
	            answer.setSelectedOption(option);
	            attempt.getAnswers().add(answer);
	        }

	        attempt.setSubmitTime(LocalDateTime.now());
	        attempt.setStatus(AttemptStatus.SUBMITTED);
	        
	        ExamAttempt saved = attemptRepository.save(attempt);
	        log.debug("Attempt {} submitted with {} answers", attemptId, saved.getAnswers().size());
	        
	        return mapper.toAttemptDto(saved);
	}

	@Transactional
	@Override
	public int autoSubmitExpiredAttempts() {
		
		 List<ExamAttempt> running = attemptRepository.findByStatus(AttemptStatus.STARTED);
	        int count = 0;
	        LocalDateTime now = LocalDateTime.now();
	        
	        for (ExamAttempt attempt : running) {
	        	
	            LocalDateTime endTime = attempt.getStartTime().plusMinutes(attempt.getExam().getDurationMinutes());
	           
	            if (now.isAfter(endTime)) {
	                attempt.setStatus(AttemptStatus.SUBMITTED);
	                attempt.setSubmitTime(now);
	                
	                attemptRepository.save(attempt);
	                log.warn("Attempt {} auto-submitted because time is over", attempt.getId());
	                count++;
	            }
	        }
	        
	        return count;
	}

}
