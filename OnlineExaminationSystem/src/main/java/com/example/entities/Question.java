package com.example.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "questions")
@Getter
@Setter
public class Question extends AuditEntity {
	
	     @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false)
	    private String questionText;

	    private Integer marks;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "exam_id")
	    private Exam exam;

	    // Question -> Options
	    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Option> options = new ArrayList<>();

}
