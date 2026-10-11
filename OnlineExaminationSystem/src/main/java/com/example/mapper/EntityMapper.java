package com.example.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.dto.AttemptResponseDto;
import com.example.dto.ExamRequestDto;
import com.example.dto.ExamResponseDto;
import com.example.dto.OptionRequestDto;
import com.example.dto.OptionResponseDto;
import com.example.dto.QuestionRequestDto;
import com.example.dto.QuestionResponseDto;
import com.example.dto.ResultResponseDto;
import com.example.dto.StudentRequestDto;
import com.example.dto.StudentResponseDto;
import com.example.entities.Exam;
import com.example.entities.ExamAttempt;
import com.example.entities.Option;
import com.example.entities.Question;
import com.example.entities.Result;
import com.example.entities.Student;

@Component
public class EntityMapper {
	
	   // ---------- Student ----------
    public Student toStudent(StudentRequestDto dto) {
        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        return student;
    }

    public StudentResponseDto toStudentDto(Student student) {
        StudentResponseDto dto = new StudentResponseDto();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setVersion(student.getVersion());
        dto.setCreatedAt(student.getCreatedAt());
        dto.setUpdatedAt(student.getUpdatedAt());
        dto.setCreatedBy(student.getCreatedBy());
        dto.setUpdatedBy(student.getUpdatedBy());
        return dto;
    }
    
    // ---------- Exam ----------
    public Exam toExam(ExamRequestDto dto) {
        Exam exam = new Exam();
        updateExam(exam, dto);
        return exam;
    }

    public void updateExam(Exam exam, ExamRequestDto dto) {
        exam.setTitle(dto.getTitle());
        exam.setDescription(dto.getDescription());
        exam.setDurationMinutes(dto.getDurationMinutes());
        exam.setPassMarks(dto.getPassMarks());
    }

    public ExamResponseDto toExamDto(Exam exam) {
        ExamResponseDto dto = new ExamResponseDto();
        dto.setId(exam.getId());
        dto.setTitle(exam.getTitle());
        dto.setDescription(exam.getDescription());
        dto.setDurationMinutes(exam.getDurationMinutes());
        dto.setPassMarks(exam.getPassMarks());
        dto.setStatus(exam.getStatus());
        return dto;
    }
    
    // ---------- Question + Options ----------
    public Question toQuestion(QuestionRequestDto dto, Exam exam) {
        Question question = new Question();
        question.setExam(exam);
        updateQuestion(question, dto);
        return question;
    }

    // replaces text, marks and all options of the question
    public void updateQuestion(Question question, QuestionRequestDto dto) {
        question.setQuestionText(dto.getQuestionText());
        question.setMarks(dto.getMarks());
        question.getOptions().clear();
        for (OptionRequestDto optionDto : dto.getOptions()) {
            Option option = new Option();
            option.setOptionText(optionDto.getOptionText());
            option.setCorrect(optionDto.isCorrect());
            option.setQuestion(question);
            question.getOptions().add(option);
        }
    }
    
    public QuestionResponseDto toQuestionDto(Question question) {
        QuestionResponseDto dto = new QuestionResponseDto();
        dto.setId(question.getId());
        dto.setQuestionText(question.getQuestionText());
        dto.setMarks(question.getMarks());
        List<OptionResponseDto> options = new ArrayList<>();
        for (Option option : question.getOptions()) {
            OptionResponseDto optionDto = new OptionResponseDto();
            optionDto.setId(option.getId());
            optionDto.setOptionText(option.getOptionText());
            options.add(optionDto);
        }
        
        dto.setOptions(options);
        return dto;
    }
    
    // ---------- Attempt ----------
    public AttemptResponseDto toAttemptDto(ExamAttempt attempt) {
        AttemptResponseDto dto = new AttemptResponseDto();
        dto.setAttemptId(attempt.getId());
        dto.setStudentId(attempt.getStudent().getId());
        dto.setExamId(attempt.getExam().getId());
        dto.setStartTime(attempt.getStartTime());
        dto.setSubmitTime(attempt.getSubmitTime());
        dto.setStatus(attempt.getStatus());
        return dto;
    }

    // ---------- Result ----------
    public ResultResponseDto toResultDto(Result result) {
        ResultResponseDto dto = new ResultResponseDto();
        dto.setAttemptId(result.getAttempt().getId());
        dto.setStudentName(result.getAttempt().getStudent().getName());
        dto.setExamTitle(result.getAttempt().getExam().getTitle());
        dto.setScore(result.getScore());
        dto.setTotalMarks(result.getTotalMarks());
        dto.setPercentage(result.getPercentage());
        dto.setStatus(result.getStatus());
        return dto;
    }

}
