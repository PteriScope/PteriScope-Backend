package com.pteriscope.webservice.security.service;

import com.pteriscope.webservice.exception.CustomException;
import com.pteriscope.webservice.security.entity.PrincipalUser;
import com.pteriscope.webservice.entities.specialist.domain.model.entity.Specialist;
import com.pteriscope.webservice.entities.specialist.domain.persistence.SpecialistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    SpecialistRepository specialistRepository;

    @Override
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {
        Specialist specialist = specialistRepository.findByDni(dni)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Specialist not found"));
        return PrincipalUser.build(specialist);
    }
}