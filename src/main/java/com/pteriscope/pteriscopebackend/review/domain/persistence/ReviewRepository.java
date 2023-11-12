package com.pteriscope.pteriscopebackend.review.domain.persistence;

import com.pteriscope.pteriscopebackend.patient.domain.model.entity.Patient;
import com.pteriscope.pteriscopebackend.review.domain.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findFirstByPatientOrderByReviewDateDesc(Patient patient);
    List<Review> getReviewsByPatientOrderByReviewDateDesc(Patient patient);
}

