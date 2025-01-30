package com.pteriscope.webservice.entities.specialist.service;

import com.pteriscope.webservice.entities.patient.domain.model.entity.Patient;
import com.pteriscope.webservice.entities.patient.domain.persistence.PatientRepository;
import com.pteriscope.webservice.entities.specialist.domain.model.entity.Specialist;
import com.pteriscope.webservice.entities.specialist.domain.persistence.SpecialistRepository;
import com.pteriscope.webservice.exception.PsRequestException;
import com.pteriscope.webservice.security.dto.JwtDto;
import com.pteriscope.webservice.security.dto.LoginUser;
import com.pteriscope.webservice.security.dto.RegisterUser;
import com.pteriscope.webservice.security.entity.Rol;
import com.pteriscope.webservice.security.enums.RolName;
import com.pteriscope.webservice.security.jwt.JwtProvider;
import com.pteriscope.webservice.security.service.RolService;
import com.pteriscope.webservice.util.PsConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SpecialistServiceImplTest {

    @Mock
    private SpecialistRepository specialistRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RolService rolService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private SpecialistServiceImpl specialistService;

    private final Long specialistId = 1L;
    private final String specialistName = "Edward";
    private final String specialistDni = "12345678";
    private final String specialistPassword = "Admin";
    private final String encodedPassword = "EncodedAdmin";
    private final Boolean showAdvice = true;

    private final RegisterUser registerUser = new RegisterUser();
    private final Specialist specialist = new Specialist();
    private final LoginUser loginUser = new LoginUser();
    private final Rol rol = new Rol();

    @BeforeEach
    void loadEntities() {
        when(passwordEncoder.encode(specialistPassword)).thenReturn(encodedPassword);
        when(passwordEncoder.matches(specialistPassword, encodedPassword)).thenReturn(true);

        registerUser.setName(specialistName);
        registerUser.setDni(specialistDni);
        registerUser.setPassword(specialistPassword);

        specialist.setId(specialistId);
        specialist.setName(specialistName);
        specialist.setDni(specialistDni);
        specialist.setPassword(passwordEncoder.encode(specialistPassword));
        specialist.setShowAdvice(showAdvice);

        loginUser.setDni(specialistDni);
        loginUser.setPassword(specialistPassword);

        rol.setRolName(RolName.ROLE_ADMIN);
    }

    @Test
    void registerSpecialist_successful() {
        when(specialistRepository.existsByDni(specialistDni)).thenReturn(false);
        when(rolService.getByRolName(any(RolName.class))).thenReturn(rol);

        String response = specialistService.registerSpecialist(registerUser);

        assertEquals(PsConstants.SPECIALIST_REGISTERED + specialistName, response);

        verify(specialistRepository, times(1)).existsByDni(specialistDni);
        verify(passwordEncoder, times(2)).encode(specialistPassword);
        verify(specialistRepository, times(1)).save(any(Specialist.class));
    }

    @Test
    void registerSpecialist_specialistDniAlreadyRegistered() {
        when(specialistRepository.existsByDni(specialistDni)).thenReturn(true);

        PsRequestException exception = assertThrows(PsRequestException.class,
                () -> specialistService.registerSpecialist(registerUser));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(PsConstants.SPECIALIST_DNI_ALREADY_REGISTERED, exception.getMessage());

        verify(specialistRepository, times(1)).existsByDni(specialistDni);
        verify(passwordEncoder, times(1)).encode(any());
        verify(specialistRepository, never()).save(any());
    }

    @Test
    void login_successful() {
        when(specialistRepository.findByDni(specialistDni)).thenReturn(Optional.of(specialist));

        JwtDto jwtDto = specialistService.login(loginUser);

        assertEquals(specialistId, jwtDto.getId());

        verify(specialistRepository, times(1)).findByDni(specialistDni);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtProvider, times(1)).generateToken(any());
    }

    @Test
    void login_specialistNotFound() {
        when(specialistRepository.findByDni(specialistDni)).thenReturn(Optional.empty());

        PsRequestException exception = assertThrows(PsRequestException.class,
                () -> specialistService.login(loginUser));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(PsConstants.SPECIALIST_NOT_FOUND, exception.getMessage());

        verify(specialistRepository, times(1)).findByDni(specialistDni);
        verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtProvider, never()).generateToken(any());
    }

    @Test
    void updateSpecialist_successful() {
        RegisterUser newSpecialist = new RegisterUser();
        newSpecialist.setName("Jordy");
        newSpecialist.setPassword("Admin1");

        Specialist expectedSpecialist = new Specialist();
        expectedSpecialist.setName(newSpecialist.getName());
        expectedSpecialist.setPassword(passwordEncoder.encode(newSpecialist.getPassword()));
        expectedSpecialist.setHospital(newSpecialist.getHospital());
        expectedSpecialist.setPosition(newSpecialist.getPosition());

        when(specialistRepository.findById(specialistId)).thenReturn(Optional.of(specialist));
        when(specialistRepository.save(any(Specialist.class))).thenReturn(expectedSpecialist);

        Specialist updatedSpecialist = specialistService.updateSpecialist(newSpecialist, specialistId);

        assertEquals(expectedSpecialist.getName(), updatedSpecialist.getName());
        assertEquals(expectedSpecialist.getPassword(), updatedSpecialist.getPassword());
        assertEquals(expectedSpecialist.getHospital(), updatedSpecialist.getHospital());
        assertEquals(expectedSpecialist.getPosition(), updatedSpecialist.getPosition());

        verify(specialistRepository, times(1)).findById(specialistId);
        verify(passwordEncoder, times(2)).encode(newSpecialist.getPassword());
        verify(specialistRepository, times(1)).save(any(Specialist.class));
    }

    @Test
    void updateSpecialist_specialistNotFound() {
        RegisterUser newSpecialist = new RegisterUser();
        newSpecialist.setName("Jordy");
        newSpecialist.setPassword("Admin1");

        when(specialistRepository.findById(specialistId)).thenReturn(Optional.empty());

        PsRequestException exception = assertThrows(PsRequestException.class,
                () -> specialistService.updateSpecialist(newSpecialist, specialistId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(PsConstants.SPECIALIST_NOT_FOUND, exception.getMessage());

        verify(specialistRepository, times(1)).findById(specialistId);
        verify(passwordEncoder, never()).encode(newSpecialist.getPassword());
        verify(specialistRepository, never()).save(any(Specialist.class));
    }

    @Test
    void getSpecialist_successful() {
        when(specialistRepository.findById(specialistId)).thenReturn(Optional.of(specialist));

        Specialist specialistGotten = specialistService.getSpecialist(specialistId);

        assertEquals(specialistId, specialistGotten.getId());

        verify(specialistRepository, times(1)).findById(specialistId);
    }

    @Test
    void getSpecialist_specialistNotFound() {
        when(specialistRepository.findById(specialistId)).thenReturn(Optional.empty());

        PsRequestException exception = assertThrows(PsRequestException.class,
                () -> specialistService.getSpecialist(specialistId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(PsConstants.SPECIALIST_NOT_FOUND, exception.getMessage());

        verify(specialistRepository, times(1)).findById(specialistId);
    }

    @Test
    void checkShowAdviceValue_successful() {
        when(specialistRepository.findById(specialistId)).thenReturn(Optional.of(specialist));

        Boolean showAdviceValueGotten = specialistService.checkShowAdviceValue(specialistId);

        assertEquals(showAdvice, showAdviceValueGotten);

        verify(specialistRepository, times(1)).findById(specialistId);
    }

    @Test
    void checkShowAdviceValue_specialistNotFound() {
        when(specialistRepository.findById(specialistId)).thenReturn(Optional.empty());

        PsRequestException exception = assertThrows(PsRequestException.class,
                () -> specialistService.checkShowAdviceValue(specialistId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(PsConstants.SPECIALIST_NOT_FOUND, exception.getMessage());

        verify(specialistRepository, times(1)).findById(specialistId);
    }

    @Test
    void markDoNotShowAdvice() {
        when(specialistRepository.findById(specialistId)).thenReturn(Optional.of(specialist));

        specialistService.markDoNotShowAdvice(specialistId);

        assertFalse(specialist.getShowAdvice());

        verify(specialistRepository, times(1)).findById(specialistId);
        verify(specialistRepository, times(1)).save(any(Specialist.class));
    }

    @Test
    void markDoNotShowAdvice_specialistNotFound() {
        when(specialistRepository.findById(specialistId)).thenReturn(Optional.empty());

        PsRequestException exception = assertThrows(PsRequestException.class,
                () -> specialistService.markDoNotShowAdvice(specialistId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(PsConstants.SPECIALIST_NOT_FOUND, exception.getMessage());

        verify(specialistRepository, times(1)).findById(specialistId);
        verify(specialistRepository, never()).save(any(Specialist.class));
    }

    @Test
    void validateCurrentPassword_successful() {
        when(specialistRepository.findById(specialistId)).thenReturn(Optional.of(specialist));

        Boolean isPasswordCorrect = specialistService.validateCurrentPassword(specialistId, specialistPassword);

        assertTrue(isPasswordCorrect);

        verify(specialistRepository, times(1)).findById(specialistId);
        verify(passwordEncoder, times(1)).matches(specialistPassword, encodedPassword);
    }

    @Test
    void validateCurrentPassword_specialistNotFound() {
        when(specialistRepository.findById(specialistId)).thenReturn(Optional.empty());

        PsRequestException exception = assertThrows(PsRequestException.class,
                () -> specialistService.validateCurrentPassword(specialistId, specialistPassword));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(PsConstants.SPECIALIST_NOT_FOUND, exception.getMessage());

        verify(specialistRepository, times(1)).findById(specialistId);
        verify(passwordEncoder, never()).matches(specialistPassword, encodedPassword);
    }

    @Test
    void deleteSpecialist_successful() {
        List<Patient> patients = List.of(new Patient(), new Patient());

        when(specialistRepository.findById(specialistId)).thenReturn(Optional.of(specialist));
        when(patientRepository.getPatientsBySpecialist(specialist)).thenReturn(patients);

        specialistService.deleteSpecialist(specialistId);

        verify(specialistRepository, times(1)).findById(specialistId);
        verify(patientRepository, times(1)).getPatientsBySpecialist(specialist);
        verify(patientRepository, times(1)).deleteAll(patients);
        verify(specialistRepository, times(1)).deleteById(specialistId);
    }

    @Test
    void deleteSpecialist_specialistNotFound() {
        when(specialistRepository.findById(specialistId)).thenReturn(Optional.empty());

        PsRequestException exception = assertThrows(
                PsRequestException.class,
                () -> specialistService.deleteSpecialist(specialistId)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(PsConstants.SPECIALIST_NOT_FOUND, exception.getMessage());

        verify(specialistRepository, times(1)).findById(specialistId);
        verify(patientRepository, never()).getPatientsBySpecialist(specialist);
        verify(patientRepository, never()).deleteAll(any());
        verify(specialistRepository, never()).deleteById(specialistId);
    }
}