package com.pteriscope.webservice.entities.patient.service;

import com.pteriscope.webservice.entities.patient.domain.model.entity.Patient;
import com.pteriscope.webservice.entities.patient.domain.persistence.PatientRepository;
import com.pteriscope.webservice.entities.specialist.domain.model.entity.Specialist;
import com.pteriscope.webservice.entities.specialist.domain.persistence.SpecialistRepository;
import com.pteriscope.webservice.exception.PsRequestException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

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
    private final String patientDni = "12345678";

    @Test
    void createPatient_successful() {
        Specialist specialist = new Specialist();
        specialist.setId(specialistId);

        Patient patient = new Patient();
        patient.setDni(patientDni);

        Mockito.when(specialistRepository.findById(specialistId)).thenReturn(Optional.of(specialist));
        Mockito.when(patientRepository.existsByDni(patientDni)).thenReturn(false);
        Mockito.when(patientRepository.save(Mockito.any(Patient.class))).thenReturn(patient);

        Patient savedPatient = patientService.createPatient(specialistId, patient);

        assertNotNull(savedPatient);
        assertEquals(patientDni, savedPatient.getDni());
        Mockito.verify(specialistRepository, Mockito.times(1)).findById(specialistId);
        Mockito.verify(patientRepository, Mockito.times(1)).existsByDni(patientDni);
        Mockito.verify(patientRepository, Mockito.times(1)).save(patient);
    }

    @Test
    void createPatient_specialistNotFound() {
        Patient patient = new Patient();
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
        Specialist specialist = new Specialist();
        specialist.setId(specialistId);

        Patient patient = new Patient();
        patient.setDni(patientDni);

        Mockito.when(specialistRepository.findById(specialistId)).thenReturn(Optional.of(specialist));
        Mockito.when(patientRepository.existsByDni(patientDni)).thenReturn(true);

        PsRequestException exception = assertThrows(PsRequestException.class,
                () -> patientService.createPatient(specialistId, patient));

        assertEquals("Ya existe un paciente con ese DNI", exception.getMessage());
        Mockito.verify(specialistRepository, Mockito.times(1)).findById(specialistId);
        Mockito.verify(patientRepository, Mockito.times(1)).existsByDni(patientDni);
        Mockito.verify(patientRepository, Mockito.never()).save(Mockito.any());
    }
}