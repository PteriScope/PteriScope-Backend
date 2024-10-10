package com.pteriscope.webservice.entities.patient.service;

import com.pteriscope.webservice.entities.patient.domain.persistence.PatientRepository;
import com.pteriscope.webservice.exception.PsRequestException;
import com.pteriscope.webservice.entities.patient.domain.model.entity.Patient;
import com.pteriscope.webservice.entities.patient.domain.serivces.PatientService;
import com.pteriscope.webservice.entities.specialist.domain.model.entity.Specialist;
import com.pteriscope.webservice.entities.specialist.domain.persistence.SpecialistRepository;
import com.pteriscope.webservice.util.PsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final SpecialistRepository specialistRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, SpecialistRepository specialistRepository) {
        this.patientRepository = patientRepository;
        this.specialistRepository = specialistRepository;
    }

    @Override
    public Patient createPatient(Long specialistId, Patient patient) {
        Optional<Specialist> specialist = specialistRepository.findById(specialistId);
        if (specialist.isPresent()) {
            if(patientRepository.existsByDni(patient.getDni()))
                throw new PsRequestException(HttpStatus.BAD_REQUEST, "Ya existe un paciente con ese DNI");

            patient.setSpecialist(specialist.get());
            return patientRepository.save(patient);
        }
        else{
            throw new PsRequestException(HttpStatus.BAD_REQUEST, "Specialist with ID " + specialistId + " does not exist");
        }
    }

    @Override
    public Patient getPatient(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new PsRequestException(HttpStatus.BAD_REQUEST, PsConstants.PATIENT_NOT_FOUND));
    }

    @Override
    public List<Patient> getPatientFromSpecialist(Long specialistId) {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new PsRequestException(HttpStatus.BAD_REQUEST, PsConstants.SPECIALIST_NOT_FOUND));
        return patientRepository.getPatientsBySpecialist(specialist);
    }

    @Override
    public Patient updatePatient(Long patientId, Patient updatedPatient) {
        Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PsRequestException(HttpStatus.BAD_REQUEST, PsConstants.PATIENT_NOT_FOUND));

        if(patientRepository.existsByDni(updatedPatient.getDni()) && !updatedPatient.getDni().equals(existingPatient.getDni()))
            throw new PsRequestException(HttpStatus.BAD_REQUEST, "Ya existe un paciente con ese DNI");

        existingPatient.setFirstName(updatedPatient.getFirstName());
        existingPatient.setLastName(updatedPatient.getLastName());
        existingPatient.setDni(updatedPatient.getDni());
        existingPatient.setAge(updatedPatient.getAge());
        existingPatient.setEmail(updatedPatient.getEmail());

        return patientRepository.save(existingPatient);
    }

    @Override
    public void deletePatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new PsRequestException(HttpStatus.BAD_REQUEST, PsConstants.PATIENT_NOT_FOUND);
        }
        patientRepository.deleteById(patientId);
    }
}
