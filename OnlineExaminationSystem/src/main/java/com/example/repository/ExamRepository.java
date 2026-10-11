package com.example.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.example.entities.Exam;
import com.example.enums.ExamStatus;


public interface ExamRepository extends JpaRepository<Exam, Long> {
	
	// JPQL query
    @Query("select e from Exam e where e.status = :status")
    Page<Exam> findByStatus(@Param("status") ExamStatus status, Pageable pageable);

}
