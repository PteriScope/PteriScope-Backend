package com.pteriscope.webservice.entities.patient.domain.serivces;

import com.pteriscope.webservice.entities.patient.domain.model.entity.Patient;

import java.util.List;

public interface PatientService {
    public Patient createPatient(Long specialistId, Patient patient);
    public Patient getPatient(Long id);
    public List<Patient> getPatientFromSpecialist(Long id);
    public Patient updatePatient(Long patientId, Patient updatedPatient);
    public void deletePatient(Long patientId);
}
