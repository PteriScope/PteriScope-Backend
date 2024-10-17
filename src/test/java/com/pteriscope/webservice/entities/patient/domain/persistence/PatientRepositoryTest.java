package com.pteriscope.webservice.entities.patient.domain.persistence;

import com.pteriscope.webservice.entities.patient.domain.model.entity.Patient;
import com.pteriscope.webservice.entities.specialist.domain.model.entity.Specialist;
import com.pteriscope.webservice.entities.specialist.domain.persistence.SpecialistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private SpecialistRepository specialistRepository;

    private final String patientFirstName = "John";
    private final String patientDni = "98765432";

    private final Specialist specialist = new Specialist(
            "Specialist",
            "12345678",
            "Admin#123",
            "Hospital del Centro",
            "Oftalmólogo",
            true
    );
    private final Patient patient = new Patient(
            patientFirstName,
            "Doe",
            patientDni,
            65,
            specialist
    );

    @BeforeEach
    void createAndSaveEntities() {
        specialistRepository.save(specialist);
        patientRepository.save(patient);
    }

    @Test
    void getPatientsBySpecialist() {
        List<Patient> patients = patientRepository.getPatientsBySpecialist(specialist);
        assertEquals(1, patients.size());
        assertEquals(patientFirstName, patients.get(0).getFirstName());
    }

    @Test
    void existsByDni() {
        boolean exists = patientRepository.existsByDni(patientDni);
        assertTrue(exists);

        boolean doesNotExist = patientRepository.existsByDni("87654321");
        assertFalse(doesNotExist);
    }
}