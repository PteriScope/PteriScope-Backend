package com.pteriscope.webservice.entities.patient.domain.persistence;

import com.pteriscope.webservice.entities.patient.domain.model.entity.Patient;
import com.pteriscope.webservice.entities.specialist.domain.model.entity.Specialist;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> getPatientsBySpecialist(Specialist specialist);
    boolean existsByDni(String dni);
}

