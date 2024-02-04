package com.pteriscope.webservice.entities.hospital.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pteriscope.webservice.entities.patient.domain.model.entity.Patient;
import com.pteriscope.webservice.security.entity.Rol;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hospitals")
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Column(unique = true)
    private String dni;

    @JsonIgnore
    @NotNull
    private String password;

    @NotNull
    private String hospital;

    @NotNull
    private String position;

    @JsonIgnore
    @OneToMany(mappedBy = "specialist", cascade = CascadeType.ALL)
    private List<Patient> patients;

    @JsonIgnore
    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_rol", joinColumns = @JoinColumn(name = "hospital_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Rol> roles = new HashSet<>();


    public Hospital(@NotNull String name, @NotNull String dni, @NotNull String password, @NotNull String hospital, @NotNull String position) {
        this.name = name;
        this.dni = dni;
        this.password = password;
        this.hospital = hospital;
        this.position = position;
    }
}
