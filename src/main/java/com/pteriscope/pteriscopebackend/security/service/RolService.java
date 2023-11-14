package com.pteriscope.pteriscopebackend.security.service;

import com.pteriscope.pteriscopebackend.security.entity.Rol;
import com.pteriscope.pteriscopebackend.security.enums.RolName;
import com.pteriscope.pteriscopebackend.security.repository.RolRepository;
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
