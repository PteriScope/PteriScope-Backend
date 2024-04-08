package com.pteriscope.webservice.entities.patient.domain.serivces;

import com.pteriscope.webservice.entities.patient.domain.model.entity.Patient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PatientService {
    CompletableFuture<Patient> createPatient(Long specialistId, Patient patient);
    CompletableFuture<Patient> getPatient(Long id);
    CompletableFuture<List<Patient>> getPatientFromSpecialist(Long id);
    CompletableFuture<Patient> updatePatient(Long patientId, Patient updatedPatient);
    void deletePatient(Long patientId);
}
