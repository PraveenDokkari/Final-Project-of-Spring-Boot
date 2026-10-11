package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.ExamAttempt;
import com.example.enums.AttemptStatus;

public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long>{
	
    boolean existsByStudentIdAndExamId(Long studentId, Long examId);

    List<ExamAttempt> findByStatus(AttemptStatus status);

}
