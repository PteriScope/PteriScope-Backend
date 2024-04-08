package com.pteriscope.webservice.entities.patient.service;

import com.pteriscope.webservice.entities.patient.domain.persistence.PatientRepository;
import com.pteriscope.webservice.exception.CustomException;
import com.pteriscope.webservice.entities.patient.domain.model.entity.Patient;
import com.pteriscope.webservice.entities.patient.domain.serivces.PatientService;
import com.pteriscope.webservice.entities.specialist.domain.model.entity.Specialist;
import com.pteriscope.webservice.entities.specialist.domain.persistence.SpecialistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private SpecialistRepository specialistRepository;

    @Override
    @Async
    public CompletableFuture<Patient> createPatient(Long specialistId, Patient patient) {
        Optional<Specialist> specialist = specialistRepository.findById(specialistId);
        if (specialist.isPresent()) {
            patient.setSpecialist(specialist.get());

            return CompletableFuture.completedFuture(patientRepository.save(patient));
        }
        else{
            throw new CustomException(HttpStatus.BAD_REQUEST, "Specialist with ID " + specialistId + " does not exist");
        }
    }

    @Override
    @Async
    public CompletableFuture<Patient> getPatient(Long patientId) {
        return CompletableFuture.completedFuture(patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Patient not found")));
    }

    @Override
    @Async
    public CompletableFuture<List<Patient>> getPatientFromSpecialist(Long specialistId) {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Specialist not found"));
        return CompletableFuture.completedFuture(patientRepository.getPatientsBySpecialist(specialist));
    }

    @Override
    @Async
    public CompletableFuture<Patient> updatePatient(Long patientId, Patient updatedPatient) {
        Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Patient not found"));

        existingPatient.setFirstName(updatedPatient.getFirstName());
        existingPatient.setLastName(updatedPatient.getLastName());

        return CompletableFuture.completedFuture(patientRepository.save(existingPatient));
    }

    @Override
    @Async
    public void deletePatient(Long patientId) {
        patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Patient not found"));
        patientRepository.deleteById(patientId);
    }
}
