package com.pteriscope.webservice.entities.specialist.domain.services;

import com.pteriscope.webservice.security.dto.JwtDto;
import com.pteriscope.webservice.security.dto.LoginUser;
import com.pteriscope.webservice.security.dto.RegisterUser;
import com.pteriscope.webservice.entities.specialist.domain.model.entity.Specialist;

import java.util.concurrent.CompletableFuture;

public interface SpecialistService {
    CompletableFuture<String> registerSpecialist(RegisterUser registerUser);

    CompletableFuture<JwtDto> login(LoginUser loginUser);

    CompletableFuture<Specialist> updateSpecialist(Specialist updatedSpecialist, Long id);

    CompletableFuture<Specialist> getSpecialist(Long specialistId);
}
