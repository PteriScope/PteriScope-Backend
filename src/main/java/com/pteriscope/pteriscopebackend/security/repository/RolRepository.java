package com.pteriscope.pteriscopebackend.security.repository;

import com.pteriscope.pteriscopebackend.security.entity.Rol;
import com.pteriscope.pteriscopebackend.security.enums.RolName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    Rol findByRolName(RolName rolName);
}
