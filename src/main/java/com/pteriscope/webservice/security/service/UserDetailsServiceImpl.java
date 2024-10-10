package com.pteriscope.webservice.security.service;

import com.pteriscope.webservice.exception.PsRequestException;
import com.pteriscope.webservice.security.entity.PrincipalUser;
import com.pteriscope.webservice.entities.specialist.domain.model.entity.Specialist;
import com.pteriscope.webservice.entities.specialist.domain.persistence.SpecialistRepository;
import com.pteriscope.webservice.util.PsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SpecialistRepository specialistRepository;

    @Autowired
    public UserDetailsServiceImpl(SpecialistRepository specialistRepository) {
        this.specialistRepository = specialistRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {
        Specialist specialist = specialistRepository.findByDni(dni)
                .orElseThrow(() -> new PsRequestException(HttpStatus.BAD_REQUEST, PsConstants.SPECIALIST_NOT_FOUND));
        return PrincipalUser.build(specialist);
    }
}