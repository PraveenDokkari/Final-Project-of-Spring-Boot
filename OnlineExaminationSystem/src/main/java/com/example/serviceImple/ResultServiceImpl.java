package com.example.serviceImple;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.ResultResponseDto;
import com.example.entities.Answer;
import com.example.entities.ExamAttempt;
import com.example.entities.Question;
import com.example.entities.Result;
import com.example.enums.AttemptStatus;
import com.example.enums.ResultStatus;
import com.example.exceptions.BadRequestException;
import com.example.exceptions.ResourceNotFoundException;
import com.example.mapper.EntityMapper;
import com.example.repository.ExamAttemptRepository;
import com.example.repository.QuestionRepository;
import com.example.repository.ResultRepository;
import com.example.service.ResultService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {
	
	 private static final Logger log = LoggerFactory.getLogger(ResultServiceImpl.class);

	    private final ResultRepository resultRepository;
	    private final ExamAttemptRepository attemptRepository;
	    private final QuestionRepository questionRepository;
	    private final EntityMapper mapper;

	
	@Transactional
	@Override
	public ResultResponseDto calculateResult(Long attemptId) {
		
		 log.info("Calculating result for attempt {}", attemptId);

	        ExamAttempt attempt = attemptRepository.findById(attemptId)
	                .orElseThrow(() -> new ResourceNotFoundException("Attempt not found with id " + attemptId));

	        if (attempt.getStatus() != AttemptStatus.SUBMITTED) {
	            throw new BadRequestException("Attempt is not submitted yet");
	        }
	        if (resultRepository.existsByAttemptId(attemptId)) {
	            throw new BadRequestException("Result already calculated for attempt " + attemptId);
	        }

	        // total marks = sum of marks of all questions in the exam
	        List<Question> questions = questionRepository.findByExamId(attempt.getExam().getId());
	        int totalMarks = 0;
	        for (Question question : questions) {
	            totalMarks += question.getMarks();
	        }
	        
	        // score = sum of marks of the correctly answered questions
	        int score = 0;
	        for (Answer answer : attempt.getAnswers()) {
	            if (answer.getSelectedOption().isCorrect()) {
	                score += answer.getQuestion().getMarks();
	            }
	        }

	        double percentage = totalMarks == 0 ? 0.0 : (score * 100.0) / totalMarks;

	        Result result = new Result();
	        result.setAttempt(attempt);
	        result.setScore(score);
	        result.setTotalMarks(totalMarks);
	        result.setPercentage(percentage);
	        result.setStatus(score >= attempt.getExam().getPassMarks() ? ResultStatus.PASS : ResultStatus.FAIL);

	        Result saved = resultRepository.save(result);
	        log.info("Result for attempt {}: {}/{} -> {}", attemptId, score, totalMarks, saved.getStatus());
	      
	        return mapper.toResultDto(saved);
	}

	@Transactional(readOnly = true)
	@Override
	public ResultResponseDto getResultByAttempt(Long attemptId) {
		
		   log.info("Fetching result of attempt {}", attemptId);
		   
	        Result result = resultRepository.findByAttemptId(attemptId)
	                .orElseThrow(() -> new ResourceNotFoundException("Result not found for attempt " + attemptId));
	        
	        return mapper.toResultDto(result);
	}

    @Transactional(readOnly = true)
	@Override
	public Page<ResultResponseDto> getRankings(Long examId, int page, int size) {
		
    	 log.info("Fetching rankings of exam {}", examId);
    	 
         return resultRepository.findRankingsByExamId(examId, PageRequest.of(page, size))
                 .map(mapper::toResultDto);
	}

	@Transactional(readOnly = true)
	@Override
	public long countByStatus(String status) {
		
		  ResultStatus resultStatus;
		  
	        try {
	            resultStatus = ResultStatus.valueOf(status.toUpperCase());
	        } 
	        catch (IllegalArgumentException ex) {
	            throw new BadRequestException("Status must be PASS or FAIL");
	        }
	        
	        return resultRepository.countByStatusNative(resultStatus.name());
	}

}
