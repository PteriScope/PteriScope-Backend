package com.pteriscope.webservice.entities.specialist.service;

import com.pteriscope.webservice.entities.specialist.domain.persistence.SpecialistRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpecialistServiceImplTest {

    @Mock
    private SpecialistRepository specialistRepository;

    @InjectMocks
    private SpecialistServiceImpl specialistService;

    @Test
    void registerSpecialist() {
    }

    @Test
    void login() {
    }

    @Test
    void updateSpecialist() {
    }

    @Test
    void getSpecialist() {
    }

    @Test
    void checkShowAdviceValue() {
    }

    @Test
    void markDoNotShowAdvice() {
    }

    @Test
    void validateCurrentPassword() {
    }
}