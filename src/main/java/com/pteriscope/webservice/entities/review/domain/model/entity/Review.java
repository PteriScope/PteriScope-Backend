package com.pteriscope.webservice.entities.review.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pteriscope.webservice.entities.patient.domain.model.entity.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    private LocalDateTime reviewDate;

    @JsonProperty("patientId")
    public Long getPatientId() {
        return (patient != null) ? patient.getId() : null;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
}

