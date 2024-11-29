package com.pteriscope.webservice.entities.specialist.domain.persistence;

import com.pteriscope.webservice.entities.specialist.domain.model.entity.Specialist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SpecialistRepositoryTest {

    @Autowired
    private SpecialistRepository specialistRepository;

    private final String specialistDni = "98765432";

    private final Specialist specialist = new Specialist(
            "Specialist",
            specialistDni,
            "Admin#123",
            "Hospital del Centro",
            "Oftalmólogo",
            true
    );

    @BeforeEach
    void createAndSaveEntities() {
        specialistRepository.save(specialist);
    }

    @Test
    void findByDni() {
        Optional<Specialist> storedSpecialist = specialistRepository.findByDni(specialistDni);
        assertNotNull(storedSpecialist);

        storedSpecialist.ifPresent(value -> {
            assertEquals(specialist.getDni(), value.getDni());
            assertEquals(specialist.getName(), value.getName());
        });
    }

    @Test
    void existsByDni() {
        boolean exists = specialistRepository.existsByDni(specialistDni);
        assertTrue(exists);

        boolean doesNotExist = specialistRepository.existsByDni("87654321");
        assertFalse(doesNotExist);
    }
}