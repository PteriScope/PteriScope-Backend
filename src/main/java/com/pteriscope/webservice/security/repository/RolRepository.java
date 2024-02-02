package com.pteriscope.webservice.security.repository;

import com.pteriscope.webservice.security.entity.Rol;
import com.pteriscope.webservice.security.enums.RolName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    Rol findByRolName(RolName rolName);
}
