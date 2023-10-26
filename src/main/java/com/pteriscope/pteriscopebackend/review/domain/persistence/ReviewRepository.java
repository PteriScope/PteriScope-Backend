package com.pteriscope.pteriscopebackend.review.domain.persistence;

import com.pteriscope.pteriscopebackend.review.domain.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}

