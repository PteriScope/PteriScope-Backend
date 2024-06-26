package com.pteriscope.webservice.entities.specialist.domain.services;

import com.pteriscope.webservice.security.dto.JwtDto;
import com.pteriscope.webservice.security.dto.LoginUser;
import com.pteriscope.webservice.security.dto.RegisterUser;
import com.pteriscope.webservice.entities.specialist.domain.model.entity.Specialist;

public interface SpecialistService {
    String registerSpecialist(RegisterUser registerUser);

    JwtDto login(LoginUser loginUser);

    Specialist updateSpecialist(RegisterUser updatedSpecialist, Long id);

    Specialist getSpecialist(Long specialistId);

    Boolean checkShowAdviceValue(Long specialistId);

    void markDoNotShowAdvice(Long specialistId);

    Boolean validateCurrentPassword(Long specialistId, String password);
}
