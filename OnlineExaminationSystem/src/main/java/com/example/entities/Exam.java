package com.example.entities;

import java.util.ArrayList;
import java.util.List;

import com.example.enums.ExamStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;

import lombok.Setter;

@Entity
@Table(name = "exams")
@Setter
@Getter
public class Exam extends AuditEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;
	
	@Column(nullable = false)
    private String title;

    private String description;

    private Integer durationMinutes;

    private Integer passMarks;
    
    @Enumerated(EnumType.STRING)
    private ExamStatus status = ExamStatus.DRAFT;
    
    // Exam -> Questions
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

}
