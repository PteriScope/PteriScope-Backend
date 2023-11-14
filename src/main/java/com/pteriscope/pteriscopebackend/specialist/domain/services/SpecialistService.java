package com.pteriscope.pteriscopebackend.specialist.domain.services;

import com.pteriscope.pteriscopebackend.security.dto.JwtDto;
import com.pteriscope.pteriscopebackend.security.dto.LoginUser;
import com.pteriscope.pteriscopebackend.security.dto.RegisterUser;
import com.pteriscope.pteriscopebackend.specialist.domain.model.entity.Specialist;
import com.pteriscope.pteriscopebackend.specialist.dto.SpecialistResponse;

public interface SpecialistService {
    String registerSpecialist(RegisterUser registerUser);

    JwtDto login(LoginUser loginUser);

    SpecialistResponse updateSpecialist(Specialist updatedSpecialist, Long id);

    SpecialistResponse getSpecialist(Long specialistId);
}
