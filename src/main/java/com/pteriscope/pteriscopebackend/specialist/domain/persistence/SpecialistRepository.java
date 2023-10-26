package com.pteriscope.pteriscopebackend.specialist.domain.persistence;

import com.pteriscope.pteriscopebackend.specialist.domain.model.entity.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SpecialistRepository extends JpaRepository<Specialist, Long> {
    Optional<Specialist> findByDni(String dni);
    boolean existsByDni(String dni);
}
