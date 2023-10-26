package com.pteriscope.pteriscopebackend.specialist.service;

import com.pteriscope.pteriscopebackend.exception.CustomException;
import com.pteriscope.pteriscopebackend.specialist.domain.model.entity.Specialist;
import com.pteriscope.pteriscopebackend.specialist.domain.persistence.SpecialistRepository;
import com.pteriscope.pteriscopebackend.specialist.domain.services.SpecialistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialistServiceImpl implements SpecialistService {
    @Autowired
    private SpecialistRepository specialistRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Specialist registerSpecialist(Specialist specialist) {
        // Check if DNI is already registered
        if (specialistRepository.findByDni(specialist.getDni()) != null) {
            throw new CustomException("Specialist with DNI " + specialist.getDni() + " is already registered");
        }
        // Encrypt the password
        specialist.setPassword(passwordEncoder.encode(specialist.getPassword()));
        return specialistRepository.save(specialist);
    }

    @Override
    public Specialist login(String dni, String password) {
        Specialist specialist = specialistRepository.findByDni(dni);
        if (specialist == null || !passwordEncoder.matches(password, specialist.getPassword())) {
            throw new CustomException("Invalid DNI or password");
        }
        return specialist;
    }

    @Override
    public Specialist updateSpecialist(Specialist updatedSpecialist, Long specialistId) {
        Specialist existingSpecialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new CustomException("Specialist not found"));

        existingSpecialist.setName(updatedSpecialist.getName());
        existingSpecialist.setHospital(updatedSpecialist.getHospital());
        existingSpecialist.setPosition(updatedSpecialist.getPosition());

        return specialistRepository.save(existingSpecialist);
    }

    @Override
    public Specialist getSpecialist(Long specialistId) {
        return specialistRepository.findById(specialistId)
                .orElseThrow(() -> new CustomException("Specialist not found"));
    }
}
