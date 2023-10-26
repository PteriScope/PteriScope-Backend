package com.pteriscope.pteriscopebackend.patient.domain.serivces;

import com.pteriscope.pteriscopebackend.patient.domain.model.entity.Patient;

import java.util.Optional;

public interface PatientService {
    public Patient createPatient(Long specialistId, Patient patient);

    public Patient getPatient(Long id);

    public Patient updatePatient(Long patientId, Patient updatedPatient);

    public void deletePatient(Long patientId);
}
