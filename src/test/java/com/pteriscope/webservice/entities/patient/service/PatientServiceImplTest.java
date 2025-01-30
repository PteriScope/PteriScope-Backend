package com.pteriscope.webservice.entities.patient.service;

import com.pteriscope.webservice.entities.patient.domain.model.entity.Patient;
import com.pteriscope.webservice.entities.patient.domain.persistence.PatientRepository;
import com.pteriscope.webservice.entities.specialist.domain.model.entity.Specialist;
import com.pteriscope.webservice.entities.specialist.domain.persistence.SpecialistRepository;
import com.pteriscope.webservice.exception.PsRequestException;
import com.pteriscope.webservice.util.PsConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private SpecialistRepository specialistRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    private final Long specialistId = 1L;
    private final Long patientId = 1L;
    private final String patientDni = "12345678";
    private final String newPatientDni = "23456789";

    Specialist specialist = new Specialist();
    Patient patient = new Patient();

    @BeforeEach
    void loadEntities() {
        specialist.setId(specialistId);
        patient.setDni(patientDni);
    }

    // createPatient Tests
    @Test
    void createPatient_successful() {
        Mockito.when(specialistRepository.findById(specialistId)).thenReturn(Optional.of(specialist));
        Mockito.when(patientRepository.existsByDni(patientDni)).thenReturn(false);
        Mockito.when(patientRepository.save(Mockito.any(Patient.class))).thenReturn(patient);

        Patient createdPatient = patientService.createPatient(specialistId, patient);

        assertNotNull(createdPatient);
        assertEquals(patientDni, createdPatient.getDni());
        Mockito.verify(specialistRepository, Mockito.times(1)).findById(specialistId);
        Mockito.verify(patientRepository, Mockito.times(1)).existsByDni(patientDni);
        Mockito.verify(patientRepository, Mockito.times(1)).save(patient);
    }

    @Test
    void createPatient_specialistNotFound() {
        Mockito.when(specialistRepository.findById(specialistId)).thenReturn(Optional.empty());

        PsRequestException exception = assertThrows(PsRequestException.class,
                () -> patientService.createPatient(specialistId, patient));

        assertEquals("Specialist with ID " + specialistId + " does not exist", exception.getMessage());
        Mockito.verify(specialistRepository, Mockito.times(1)).findById(specialistId);
        Mockito.verify(patientRepository, Mockito.never()).existsByDni(patientDni);
        Mockito.verify(patientRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void createPatient_duplicateDni() {
        Mockito.when(specialistRepository.findById(specialistId)).thenReturn(Optional.of(specialist));
        Mockito.when(patientRepository.existsByDni(patientDni)).thenReturn(true);

        PsRequestException exception = assertThrows(PsRequestException.class,
                () -> patientService.createPatient(specialistId, patient));

        assertEquals(PsConstants.PATIENT_DNI_ALREADY_REGISTERED, exception.getMessage());
        Mockito.verify(specialistRepository, Mockito.times(1)).findById(specialistId);
        Mockito.verify(patientRepository, Mockito.times(1)).existsByDni(patientDni);
        Mockito.verify(patientRepository, Mockito.never()).save(Mockito.any());
    }

    // createPatient Tests
    @Test
    void getPatient_successful() {
        Mockito.when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        Patient storedPatient = patientService.getPatient(patientId);

        assertNotNull(storedPatient);
        assertEquals(patientDni, storedPatient.getDni());
        Mockito.verify(patientRepository, Mockito.times(1)).findById(patientId);
    }

    @Test
    void getPatient_patientNotFound() {
        Mockito.when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        PsRequestException exception = assertThrows(PsRequestException.class,
                () -> patientService.getPatient(patientId));

        assertEquals(PsConstants.PATIENT_NOT_FOUND, exception.getMessage());
        Mockito.verify(patientRepository, Mockito.times(1)).findById(specialistId);
    }

    // getPatientFromSpecialist Tests
    @Test
    void getPatientFromSpecialist_successful() {
        List<Patient> patients = new ArrayList<>();
        patients.add(patient);

        Mockito.when(specialistRepository.findById(specialistId)).thenReturn(Optional.of(specialist));
        Mockito.when(patientRepository.getPatientsBySpecialist(specialist)).thenReturn(patients);

        List<Patient> storedPatients = patientService.getPatientFromSpecialist(specialistId);
        assertEquals(1, storedPatients.size());
        assertEquals(patient.getDni(), storedPatients.getFirst().getDni());
        Mockito.verify(specialistRepository, Mockito.times(1)).findById(specialistId);
        Mockito.verify(patientRepository, Mockito.times(1)).getPatientsBySpecialist(specialist);
    }

    @Test
    void getPatientFromSpecialist_specialistNotFound() {
        Mockito.when(specialistRepository.findById(specialistId)).thenReturn(Optional.empty());

        PsRequestException exception = assertThrows(PsRequestException.class,
                () -> patientService.getPatientFromSpecialist(specialistId));

        assertEquals(PsConstants.SPECIALIST_NOT_FOUND, exception.getMessage());

        Mockito.verify(specialistRepository, Mockito.times(1)).findById(specialistId);
        Mockito.verify(patientRepository, Mockito.never()).getPatientsBySpecialist(specialist);
    }

    // updatePatient Tests
    @Test
    void updatePatient_successful_1() {
        Patient updatedPatientRequest = new Patient();
        updatedPatientRequest.setDni(newPatientDni);
        updatedPatientRequest.setFirstName("Edward");

        Mockito.when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        Mockito.when(patientRepository.existsByDni(newPatientDni)).thenReturn(false);
        Mockito.when(patientRepository.save(Mockito.any(Patient.class))).thenReturn(updatedPatientRequest);

        Patient updatedPatientResponse = patientService.updatePatient(patientId, updatedPatientRequest);

        assertEquals(updatedPatientRequest.getFirstName(), updatedPatientResponse.getFirstName());
        assertEquals(updatedPatientRequest.getLastName(), updatedPatientResponse.getLastName());
        assertEquals(updatedPatientRequest.getDni(), updatedPatientResponse.getDni());
        assertEquals(updatedPatientRequest.getAge(), updatedPatientResponse.getAge());
        assertEquals(updatedPatientRequest.getEmail(), updatedPatientResponse.getEmail());

        Mockito.verify(patientRepository, Mockito.times(1)).findById(patientId);
        Mockito.verify(patientRepository, Mockito.times(1)).existsByDni(newPatientDni);
        Mockito.verify(patientRepository, Mockito.times(1)).save(patient);
    }

    @Test
    void updatePatient_successful_2() {
        Patient updatedPatientRequest = new Patient();
        updatedPatientRequest.setDni(patientDni);
        updatedPatientRequest.setFirstName("Edward");

        Mockito.when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        Mockito.when(patientRepository.existsByDni(patientDni)).thenReturn(true);
        Mockito.when(patientRepository.save(Mockito.any(Patient.class))).thenReturn(updatedPatientRequest);

        Patient updatedPatientResponse = patientService.updatePatient(patientId, updatedPatientRequest);

        assertEquals(updatedPatientRequest.getFirstName(), updatedPatientResponse.getFirstName());
        assertEquals(updatedPatientRequest.getLastName(), updatedPatientResponse.getLastName());
        assertEquals(updatedPatientRequest.getDni(), updatedPatientResponse.getDni());
        assertEquals(updatedPatientRequest.getAge(), updatedPatientResponse.getAge());
        assertEquals(updatedPatientRequest.getEmail(), updatedPatientResponse.getEmail());

        Mockito.verify(patientRepository, Mockito.times(1)).findById(patientId);
        Mockito.verify(patientRepository, Mockito.times(1)).existsByDni(patientDni);
        Mockito.verify(patientRepository, Mockito.times(1)).save(patient);
    }

    @Test
    void updatePatient_patientNotFound() {
        Patient updatedPatientRequest = new Patient();
        updatedPatientRequest.setDni(newPatientDni);
        updatedPatientRequest.setFirstName("Edward");

        Mockito.when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        PsRequestException exception = assertThrows(PsRequestException.class,
                () -> patientService.updatePatient(patientId, updatedPatientRequest));

        assertEquals(PsConstants.PATIENT_NOT_FOUND, exception.getMessage());

        Mockito.verify(patientRepository, Mockito.times(1)).findById(patientId);
        Mockito.verify(patientRepository, Mockito.never()).existsByDni(patientDni);
        Mockito.verify(patientRepository, Mockito.never()).save(patient);
    }

    @Test
    void updatePatient_dniAlreadyRegistered() {
        Patient updatedPatientRequest = new Patient();
        updatedPatientRequest.setDni(newPatientDni);
        updatedPatientRequest.setFirstName("Edward");

        Mockito.when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        Mockito.when(patientRepository.existsByDni(newPatientDni)).thenReturn(true);

        PsRequestException exception = assertThrows(PsRequestException.class,
                () -> patientService.updatePatient(patientId, updatedPatientRequest));

        assertEquals(PsConstants.PATIENT_DNI_ALREADY_REGISTERED, exception.getMessage());

        Mockito.verify(patientRepository, Mockito.times(1)).findById(patientId);
        Mockito.verify(patientRepository, Mockito.times(1)).existsByDni(newPatientDni);
        Mockito.verify(patientRepository, Mockito.never()).save(patient);
    }

    // deletePatient Tests
    @Test
    void deletePatient_successful() {
        Mockito.when(patientRepository.existsById(patientId)).thenReturn(true);

        patientService.deletePatient(patientId);

        Mockito.verify(patientRepository, Mockito.times(1)).existsById(patientId);
        Mockito.verify(patientRepository, Mockito.times(1)).deleteById(patientId);
    }

    @Test
    void deletePatient_patientNotFound() {
        Mockito.when(patientRepository.existsById(patientId)).thenReturn(false);

        PsRequestException exception = assertThrows(PsRequestException.class,
                () -> patientService.deletePatient(patientId)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(PsConstants.PATIENT_NOT_FOUND, exception.getMessage());

        Mockito.verify(patientRepository, Mockito.times(1)).existsById(patientId);
        Mockito.verify(patientRepository, Mockito.never()).deleteById(patientId);
    }
}