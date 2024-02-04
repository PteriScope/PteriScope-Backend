package com.pteriscope.webservice.entities.hospital.service;

import com.pteriscope.webservice.entities.hospital.domain.model.entity.Hospital;
import com.pteriscope.webservice.entities.hospital.domain.persistence.HospitalRepository;
import com.pteriscope.webservice.entities.hospital.domain.services.HospitalService;
import com.pteriscope.webservice.exception.CustomException;
import com.pteriscope.webservice.security.dto.JwtDto;
import com.pteriscope.webservice.security.dto.LoginUser;
import com.pteriscope.webservice.security.dto.RegisterUser;
import com.pteriscope.webservice.security.entity.Rol;
import com.pteriscope.webservice.security.enums.RolName;
import com.pteriscope.webservice.security.jwt.JwtProvider;
import com.pteriscope.webservice.security.service.RolService;
import com.pteriscope.webservice.entities.specialist.domain.model.entity.Specialist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    RolService rolService;

    @Override
    public String registerHospital(RegisterUser registerUser) {
        if(hospitalRepository.existsByDni(registerUser.dni))
            throw new CustomException(HttpStatus.BAD_REQUEST, "Ya existe un usuario con ese DNI");

        Hospital hospital = new Hospital(
                registerUser.name,
                registerUser.dni,
                passwordEncoder.encode(registerUser.password),
                registerUser.hospital,
                registerUser.position
        );

        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolName(RolName.ROLE_USER));
        roles.add(rolService.getByRolName(RolName.ROLE_ADMIN));
        hospital.setRoles(roles);
        hospitalRepository.save(hospital);
        return hospital.getName() + "Usuario creado";
    }

    @Override
    public JwtDto login(LoginUser loginUser){
        Hospital hospital = hospitalRepository.findByDni(loginUser.getDni())
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Hospital not found"));
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getDni(), loginUser.getPassword()));
        //SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        return new JwtDto(jwt, hospital.getId());
    }

    @Override
    public Hospital updateHospital(Hospital updatedHospital, Long hospitalId) {
        Hospital existingHospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Hospital not found"));

        existingHospital.setName(updatedHospital.getName());
        existingHospital.setHospital(updatedHospital.getHospital());
        existingHospital.setPosition(updatedHospital.getPosition());

        return hospitalRepository.save(existingHospital);
    }

    @Override
    public Hospital getHospital(Long hospitalId) {
        return hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Hospital not found"));
    }
}
