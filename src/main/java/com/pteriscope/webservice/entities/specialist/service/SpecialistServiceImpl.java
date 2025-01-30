package com.pteriscope.webservice.entities.specialist.service;

import com.pteriscope.webservice.entities.patient.domain.model.entity.Patient;
import com.pteriscope.webservice.entities.patient.domain.persistence.PatientRepository;
import com.pteriscope.webservice.entities.specialist.domain.persistence.SpecialistRepository;
import com.pteriscope.webservice.entities.specialist.domain.services.SpecialistService;
import com.pteriscope.webservice.exception.PsRequestException;
import com.pteriscope.webservice.security.dto.JwtDto;
import com.pteriscope.webservice.security.dto.LoginUser;
import com.pteriscope.webservice.security.dto.RegisterUser;
import com.pteriscope.webservice.security.entity.Rol;
import com.pteriscope.webservice.security.enums.RolName;
import com.pteriscope.webservice.security.jwt.JwtProvider;
import com.pteriscope.webservice.security.service.RolService;
import com.pteriscope.webservice.entities.specialist.domain.model.entity.Specialist;
import com.pteriscope.webservice.util.PsConstants;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SpecialistServiceImpl implements SpecialistService {
    private final SpecialistRepository specialistRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RolService rolService;

    @Autowired
    public SpecialistServiceImpl(SpecialistRepository specialistRepository,
                                 PatientRepository patientRepository,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 JwtProvider jwtProvider,
                                 RolService rolService) {
        this.specialistRepository = specialistRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.rolService = rolService;
    }

    @Override
    public String registerSpecialist(@NotNull RegisterUser registerUser) {
        if(specialistRepository.existsByDni(registerUser.getDni()))
            throw new PsRequestException(HttpStatus.BAD_REQUEST, PsConstants.SPECIALIST_DNI_ALREADY_REGISTERED);

        Specialist specialist = new Specialist(
                registerUser.getName(),
                registerUser.getDni(),
                passwordEncoder.encode(registerUser.getPassword()),
                registerUser.getHospital(),
                registerUser.getPosition(),
                true
        );

        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolName(RolName.ROLE_USER));
        roles.add(rolService.getByRolName(RolName.ROLE_ADMIN));
        specialist.setRoles(roles);
        specialistRepository.save(specialist);
        return PsConstants.SPECIALIST_REGISTERED + specialist.getName();
    }

    @Override
    public JwtDto login(@NotNull LoginUser loginUser){
        Specialist specialist = specialistRepository.findByDni(loginUser.getDni())
                .orElseThrow(() -> new PsRequestException(HttpStatus.NOT_FOUND, PsConstants.SPECIALIST_NOT_FOUND));
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getDni(), loginUser.getPassword()));
        String jwt = jwtProvider.generateToken(authentication);
        return new JwtDto(jwt, specialist.getId());
    }

    @Override
    public Specialist updateSpecialist(@NotNull RegisterUser updatedSpecialist, Long specialistId) {
        Specialist existingSpecialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new PsRequestException(HttpStatus.NOT_FOUND, PsConstants.SPECIALIST_NOT_FOUND));

        existingSpecialist.setName(updatedSpecialist.getName());
        existingSpecialist.setPassword(passwordEncoder.encode(updatedSpecialist.getPassword()));
        existingSpecialist.setHospital(updatedSpecialist.getHospital());
        existingSpecialist.setPosition(updatedSpecialist.getPosition());

        return specialistRepository.save(existingSpecialist);
    }

    @Override
    public Specialist getSpecialist(Long specialistId) {
        return specialistRepository.findById(specialistId)
                .orElseThrow(() -> new PsRequestException(HttpStatus.NOT_FOUND, PsConstants.SPECIALIST_NOT_FOUND));
    }

    @Override
    public Boolean checkShowAdviceValue(Long specialistId) {
        Specialist existingSpecialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new PsRequestException(HttpStatus.NOT_FOUND, PsConstants.SPECIALIST_NOT_FOUND));
        return existingSpecialist.getShowAdvice();
    }

    /*
    * TODO: MEJORAR USANDO QUERY EN REPOSITORIO:
    * @Modifying
    * @Query("UPDATE Specialist s SET s.showAdvice = false WHERE s.id = :specialistId")
    * void updateShowAdviceFalse(@Param("specialistId") Long specialistId);
    * */
    @Override
    public void markDoNotShowAdvice(Long specialistId) {
        Specialist existingSpecialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new PsRequestException(HttpStatus.NOT_FOUND, PsConstants.SPECIALIST_NOT_FOUND));

        existingSpecialist.setShowAdvice(false);

        specialistRepository.save(existingSpecialist);
    }

    @Override
    public Boolean validateCurrentPassword(Long specialistId, String password) {
        Specialist existingSpecialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new PsRequestException(HttpStatus.NOT_FOUND, PsConstants.SPECIALIST_NOT_FOUND));
        return passwordEncoder.matches(password, existingSpecialist.getPassword());
    }

    @Override
    public void deleteSpecialist(Long specialistId) {
        Specialist existingSpecialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new PsRequestException(HttpStatus.NOT_FOUND, PsConstants.SPECIALIST_NOT_FOUND));

        List<Patient> patients = patientRepository.getPatientsBySpecialist(existingSpecialist);
        patientRepository.deleteAll(patients);
        specialistRepository.deleteById(specialistId);
    }
}
