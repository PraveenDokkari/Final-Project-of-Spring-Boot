package com.example.serviceImple;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.OptionRequestDto;
import com.example.dto.QuestionRequestDto;
import com.example.dto.QuestionResponseDto;
import com.example.entities.Exam;
import com.example.entities.Question;
import com.example.exceptions.BadRequestException;
import com.example.exceptions.ResourceNotFoundException;
import com.example.mapper.EntityMapper;
import com.example.repository.ExamRepository;
import com.example.repository.QuestionRepository;
import com.example.service.QuestionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService{
	
	private static final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuestionRepository questionRepository;
    private final ExamRepository examRepository;
    private final EntityMapper mapper;
    
    
    private void validateOneCorrectOption(QuestionRequestDto dto) {
    	
        long correctCount = dto.getOptions().stream().filter(OptionRequestDto::isCorrect).count();
        
        if (correctCount != 1) {
            throw new BadRequestException("A question must have exactly one correct option");
        }
    }
	
    @Transactional
	@Override
	public QuestionResponseDto addQuestion(Long examId, QuestionRequestDto dto) {
		
        log.info("Adding question to exam {}", examId);
        
        validateOneCorrectOption(dto);
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id " + examId));
        
        Question saved = questionRepository.save(mapper.toQuestion(dto, exam));
        log.debug("Question created with id {}", saved.getId());
        
        return mapper.toQuestionDto(saved);
	}

    @Transactional
	@Override
	public QuestionResponseDto updateQuestion(Long questionId, QuestionRequestDto dto) {
		
      	 log.info("Updating question {}", questionId);
      	 
         validateOneCorrectOption(dto);
         Question question = questionRepository.findById(questionId)
                 .orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));
        
         mapper.updateQuestion(question, dto);
         
         return mapper.toQuestionDto(questionRepository.save(question));
	}

    @Transactional(readOnly = true)
	@Override
	public List<QuestionResponseDto> getQuestionsByExam(Long examId) {
		
    	 log.info("Fetching questions of exam {}", examId);
    	 
         if (!examRepository.existsById(examId)) {
             throw new ResourceNotFoundException("Exam not found with id " + examId);
         }
         return questionRepository.findByExamId(examId).stream()
                 .map(mapper::toQuestionDto)
                 .collect(Collectors.toList());
	}

}
