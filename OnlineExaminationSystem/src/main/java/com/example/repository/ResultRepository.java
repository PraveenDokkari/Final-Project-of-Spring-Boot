package com.example.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entities.Result;

public interface ResultRepository extends JpaRepository<Result, Long>{
	
	boolean existsByAttemptId(Long attemptId);

    Optional<Result> findByAttemptId(Long attemptId);

    // JPQL query : ranking (highest score first)
    @Query("select r from Result r where r.attempt.exam.id = :examId order by r.score desc")
    Page<Result> findRankingsByExamId(@Param("examId") Long examId, Pageable pageable);

    // Native query : count results by status (PASS / FAIL)
    @Query(value = "select count(*) from results where status = :status", nativeQuery = true)
    long countByStatusNative(@Param("status") String status);

}
