package com.pteriscope.webservice.entities.patient.domain.serivces;

import com.pteriscope.webservice.entities.patient.domain.model.entity.Patient;

import java.util.List;

public interface PatientService {
    Patient createPatient(Long specialistId, Patient patient);
    Patient getPatient(Long id);
    List<Patient> getPatientFromSpecialist(Long id);
    Patient updatePatient(Long patientId, Patient updatedPatient);
    void deletePatient(Long patientId);
}
