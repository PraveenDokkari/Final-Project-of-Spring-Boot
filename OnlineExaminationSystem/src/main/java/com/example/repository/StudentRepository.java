package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.entities.Student;


public interface StudentRepository extends JpaRepository<Student, Long>  {

	boolean existsByEmail(String email);
	
}
