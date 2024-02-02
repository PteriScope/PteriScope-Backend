package com.pteriscope.webservice.security.service;

import com.pteriscope.webservice.security.entity.Rol;
import com.pteriscope.webservice.security.enums.RolName;
import com.pteriscope.webservice.security.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RolService {

    @Autowired
    RolRepository rolRepository;

    public Rol getByRolName(RolName rolName){
        return rolRepository.findByRolName(rolName);
    }

    public void save(Rol rol){
        rolRepository.save(rol);
    }
}
