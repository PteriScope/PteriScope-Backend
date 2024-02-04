package com.pteriscope.webservice.entities.hospital.domain.persistence;

import com.pteriscope.webservice.entities.hospital.domain.model.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    Optional<Hospital> findByDni(String dni);
    boolean existsByDni(String dni);
}
