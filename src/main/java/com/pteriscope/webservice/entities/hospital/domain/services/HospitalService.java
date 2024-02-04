package com.pteriscope.webservice.entities.hospital.domain.services;

import com.pteriscope.webservice.entities.hospital.domain.model.entity.Hospital;
import com.pteriscope.webservice.security.dto.JwtDto;
import com.pteriscope.webservice.security.dto.LoginUser;
import com.pteriscope.webservice.security.dto.RegisterUser;

public interface HospitalService {
    String registerHospital(RegisterUser registerUser);

    JwtDto login(LoginUser loginUser);

    Hospital updateHospital(Hospital updatedHospital, Long id);

    Hospital getHospital(Long hospitalId);
}
