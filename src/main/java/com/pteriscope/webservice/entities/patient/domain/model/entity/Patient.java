package com.pteriscope.webservice.entities.patient.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pteriscope.webservice.entities.review.domain.model.entity.Review;
import com.pteriscope.webservice.entities.specialist.domain.model.entity.Specialist;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String dni;

    @NotNull
    private Integer age;
    
    private String email;

    private String lastReviewResult;

    private LocalDateTime lastReviewDate;

    @JsonProperty("specialistId")
    public Long getSpecialistId() {
        return (specialist != null) ? specialist.getId() : null;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "specialist_id", nullable = false)
    private Specialist specialist;

    @JsonIgnore
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private Set<Review> reviews;
}
