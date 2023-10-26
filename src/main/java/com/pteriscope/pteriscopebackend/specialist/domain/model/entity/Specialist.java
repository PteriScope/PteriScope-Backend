package com.pteriscope.pteriscopebackend.specialist.domain.model.entity;

import com.pteriscope.pteriscopebackend.patient.domain.model.entity.Patient;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "specialists")
public class Specialist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String dni;

    @NotBlank
    private String password;

    @NotBlank
    private String hospital;

    @NotBlank
    private String position;

    @OneToMany(mappedBy = "specialist", cascade = CascadeType.ALL)
    private List<Patient> patients;
}
