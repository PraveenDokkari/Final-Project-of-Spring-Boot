package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
	
	List<Question> findByExamId(Long examId);


}
