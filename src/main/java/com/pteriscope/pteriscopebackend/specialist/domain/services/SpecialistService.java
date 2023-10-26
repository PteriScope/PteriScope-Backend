package com.pteriscope.pteriscopebackend.specialist.domain.services;

import com.pteriscope.pteriscopebackend.security.dto.JwtDto;
import com.pteriscope.pteriscopebackend.security.dto.LoginUser;
import com.pteriscope.pteriscopebackend.security.dto.RegisterUser;
import com.pteriscope.pteriscopebackend.specialist.domain.model.entity.Specialist;
import com.pteriscope.pteriscopebackend.specialist.dto.SpecialistResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface SpecialistService {
    public String registerSpecialist(RegisterUser registerUser);

    public JwtDto login(LoginUser loginUser);

    public SpecialistResponse updateSpecialist(Specialist updatedSpecialist, Long id);

    public SpecialistResponse getSpecialist(Long specialistId);
}
