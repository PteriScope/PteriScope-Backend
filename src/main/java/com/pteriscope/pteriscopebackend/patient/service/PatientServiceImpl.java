package com.pteriscope.pteriscopebackend.patient.service;

import com.pteriscope.pteriscopebackend.exception.CustomException;
import com.pteriscope.pteriscopebackend.patient.domain.model.entity.Patient;
import com.pteriscope.pteriscopebackend.patient.domain.persistence.PatientRepository;
import com.pteriscope.pteriscopebackend.patient.domain.serivces.PatientService;
import com.pteriscope.pteriscopebackend.specialist.domain.model.entity.Specialist;
import com.pteriscope.pteriscopebackend.specialist.domain.persistence.SpecialistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private SpecialistRepository specialistRepository;

    @Override
    public Patient createPatient(Long specialistId, Patient patient) {
        Optional<Specialist> specialist = specialistRepository.findById(specialistId);
        if (specialist.isPresent()) {
            patient.setSpecialist(specialist.get());
            return patientRepository.save(patient);
        }
        else{
            throw new CustomException("Specialist with ID " + specialistId + " does not exist");
        }
    }

    @Override
    public Patient getPatient(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException("Patient not found"));
    }

    @Override
    public Patient updatePatient(Long patientId, Patient updatedPatient) {
        Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException("Patient not found"));

        existingPatient.setFirstName(updatedPatient.getFirstName());
        existingPatient.setLastName(updatedPatient.getLastName());

        return patientRepository.save(existingPatient);
    }

    @Override
    public void deletePatient(Long patientId) {
        Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException("Patient not found"));
        patientRepository.deleteById(patientId);
    }
}
