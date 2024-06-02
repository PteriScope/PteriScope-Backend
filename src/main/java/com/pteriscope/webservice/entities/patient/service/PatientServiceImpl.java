package com.pteriscope.webservice.entities.patient.service;

import com.pteriscope.webservice.entities.patient.domain.persistence.PatientRepository;
import com.pteriscope.webservice.exception.CustomException;
import com.pteriscope.webservice.entities.patient.domain.model.entity.Patient;
import com.pteriscope.webservice.entities.patient.domain.serivces.PatientService;
import com.pteriscope.webservice.entities.specialist.domain.model.entity.Specialist;
import com.pteriscope.webservice.entities.specialist.domain.persistence.SpecialistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
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
            if(patientRepository.existsByDni(patient.getDni()))
                throw new CustomException(HttpStatus.BAD_REQUEST, "Ya existe un paciente con ese DNI");

            patient.setSpecialist(specialist.get());
            return patientRepository.save(patient);
        }
        else{
            throw new CustomException(HttpStatus.BAD_REQUEST, "Specialist with ID " + specialistId + " does not exist");
        }
    }

    @Override
    public Patient getPatient(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Patient not found"));
    }

    @Override
    public List<Patient> getPatientFromSpecialist(Long specialistId) {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Specialist not found"));
        return patientRepository.getPatientsBySpecialist(specialist);
    }

    @Override
    public Patient updatePatient(Long patientId, Patient updatedPatient) {
        Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Patient not found"));

        if(patientRepository.existsByDni(updatedPatient.getDni()))
            throw new CustomException(HttpStatus.BAD_REQUEST, "Ya existe un paciente con ese DNI");

        existingPatient.setFirstName(updatedPatient.getFirstName());
        existingPatient.setLastName(updatedPatient.getLastName());
        existingPatient.setDni(updatedPatient.getDni());
        existingPatient.setAge(updatedPatient.getAge());
        existingPatient.setEmail(updatedPatient.getEmail());

        return patientRepository.save(existingPatient);
    }

    @Override
    public void deletePatient(Long patientId) {
        patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Patient not found"));
        patientRepository.deleteById(patientId);
    }
}
