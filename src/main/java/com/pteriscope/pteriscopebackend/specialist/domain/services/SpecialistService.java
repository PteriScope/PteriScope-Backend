package com.pteriscope.pteriscopebackend.specialist.domain.services;

import com.pteriscope.pteriscopebackend.specialist.domain.model.entity.Specialist;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface SpecialistService {
    public Specialist registerSpecialist(Specialist specialist);

    public Specialist login(String dni, String password);

    public Specialist updateSpecialist(Specialist updatedSpecialist, Long id);

    public Specialist getSpecialist(Long specialistId);
}
