package com.pteriscope.pteriscopebackend.patient.service;

import com.pteriscope.pteriscopebackend.exception.CustomException;
import com.pteriscope.pteriscopebackend.patient.domain.model.entity.Patient;
import com.pteriscope.pteriscopebackend.patient.domain.persistence.PatientRepository;
import com.pteriscope.pteriscopebackend.patient.domain.serivces.PatientService;
import com.pteriscope.pteriscopebackend.patient.dto.PatientResponse;
import com.pteriscope.pteriscopebackend.specialist.domain.model.entity.Specialist;
import com.pteriscope.pteriscopebackend.specialist.domain.persistence.SpecialistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private SpecialistRepository specialistRepository;

    @Override
    public PatientResponse createPatient(Long specialistId, Patient patient) {
        Optional<Specialist> specialist = specialistRepository.findById(specialistId);
        if (specialist.isPresent()) {
            patient.setSpecialist(specialist.get());
            Patient savedPatient = patientRepository.save(patient);

            return mapPatient(savedPatient);
        }
        else{
            throw new CustomException(HttpStatus.BAD_REQUEST, "Specialist with ID " + specialistId + " does not exist");
        }
    }

    @Override
    public PatientResponse getPatient(Long patientId) {
        Patient storedPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Patient not found"));

        return mapPatient(storedPatient);
    }

    private PatientResponse mapPatient(Patient patient){
        PatientResponse response = new PatientResponse();
        response.setId(patient.getId());
        response.setFirstName(patient.getFirstName());
        response.setLastName(patient.getLastName());
        response.setDni(patient.getDni());
        response.setAge(patient.getAge());
        response.setEmail(patient.getEmail());
        response.setSpecialistId(patient.getSpecialist().getId());
        return response;
    }

    @Override
    public PatientResponse updatePatient(Long patientId, Patient updatedPatient) {
        Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Patient not found"));

        existingPatient.setFirstName(updatedPatient.getFirstName());
        existingPatient.setLastName(updatedPatient.getLastName());

        return mapPatient(patientRepository.save(existingPatient));
    }

    @Override
    public void deletePatient(Long patientId) {
        Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Patient not found"));
        patientRepository.deleteById(patientId);
    }
}
