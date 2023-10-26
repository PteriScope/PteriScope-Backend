package com.pteriscope.pteriscopebackend.patient.domain.serivces;

import com.pteriscope.pteriscopebackend.patient.domain.model.entity.Patient;
import com.pteriscope.pteriscopebackend.patient.dto.PatientResponse;

import java.util.Optional;

public interface PatientService {
    public PatientResponse createPatient(Long specialistId, Patient patient);

    public PatientResponse getPatient(Long id);

    public PatientResponse updatePatient(Long patientId, Patient updatedPatient);

    public void deletePatient(Long patientId);
}
