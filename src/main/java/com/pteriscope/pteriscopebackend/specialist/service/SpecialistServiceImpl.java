package com.pteriscope.pteriscopebackend.specialist.service;

import com.pteriscope.pteriscopebackend.exception.CustomException;
import com.pteriscope.pteriscopebackend.patient.domain.model.entity.Patient;
import com.pteriscope.pteriscopebackend.patient.dto.PatientResponse;
import com.pteriscope.pteriscopebackend.security.dto.JwtDto;
import com.pteriscope.pteriscopebackend.security.dto.LoginUser;
import com.pteriscope.pteriscopebackend.security.dto.RegisterUser;
import com.pteriscope.pteriscopebackend.security.entity.Rol;
import com.pteriscope.pteriscopebackend.security.enums.RolName;
import com.pteriscope.pteriscopebackend.security.jwt.JwtProvider;
import com.pteriscope.pteriscopebackend.security.service.RolService;
import com.pteriscope.pteriscopebackend.specialist.domain.model.entity.Specialist;
import com.pteriscope.pteriscopebackend.specialist.domain.persistence.SpecialistRepository;
import com.pteriscope.pteriscopebackend.specialist.domain.services.SpecialistService;
import com.pteriscope.pteriscopebackend.specialist.dto.SpecialistResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SpecialistServiceImpl implements SpecialistService {
    @Autowired
    private SpecialistRepository specialistRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    RolService rolService;

    @Override
    public String registerSpecialist(RegisterUser registerUser) {
        if(specialistRepository.existsByDni(registerUser.dni))
            throw new CustomException(HttpStatus.BAD_REQUEST, "Ya existe un usuario con ese DNI");

        Specialist specialist = new Specialist(
                registerUser.nombre,
                registerUser.dni,
                passwordEncoder.encode(registerUser.password),
                registerUser.hospital,
                registerUser.position
        );

        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolName(RolName.ROLE_USER));
        roles.add(rolService.getByRolName(RolName.ROLE_ADMIN));
        specialist.setRoles(roles);
        specialistRepository.save(specialist);
        return specialist.getName() + "Usuario creado";
    }

    @Override
    public JwtDto login(LoginUser loginUser){
        Specialist specialist = specialistRepository.findByDni(loginUser.getDNI())
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Specialist not found"));
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getDNI(), loginUser.getPassword()));
        //SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        return new JwtDto(jwt, specialist.getId());
    }

    @Override
    public SpecialistResponse updateSpecialist(Specialist updatedSpecialist, Long specialistId) {
        Specialist existingSpecialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Specialist not found"));

        existingSpecialist.setName(updatedSpecialist.getName());
        existingSpecialist.setHospital(updatedSpecialist.getHospital());
        existingSpecialist.setPosition(updatedSpecialist.getPosition());

        return mapSpecialist(specialistRepository.save(existingSpecialist));
    }

    @Override
    public SpecialistResponse getSpecialist(Long specialistId) {
        Specialist storedSpecialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Specialist not found"));

        return mapSpecialist(storedSpecialist);
    }

    private SpecialistResponse mapSpecialist(Specialist specialist){
        SpecialistResponse response = new SpecialistResponse();
        response.setId(specialist.getId());
        response.setName(specialist.getName());
        response.setDni(specialist.getDni());
        response.setHospital(specialist.getHospital());
        response.setPosition(specialist.getPosition());
        return response;
    }
}
