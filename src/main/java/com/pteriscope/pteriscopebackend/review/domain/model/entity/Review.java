package com.pteriscope.pteriscopebackend.review.domain.model.entity;

import com.pteriscope.pteriscopebackend.patient.domain.model.entity.Patient;
import com.pteriscope.pteriscopebackend.specialist.domain.model.entity.Specialist;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String imageBase64;
    private String reviewResult;
    private LocalDate reviewDate;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
}

