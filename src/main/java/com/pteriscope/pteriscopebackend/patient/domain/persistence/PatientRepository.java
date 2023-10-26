package com.pteriscope.pteriscopebackend.patient.domain.persistence;

import com.pteriscope.pteriscopebackend.patient.domain.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
}

