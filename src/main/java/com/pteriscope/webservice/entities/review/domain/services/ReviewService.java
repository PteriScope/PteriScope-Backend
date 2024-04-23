package com.pteriscope.webservice.entities.review.domain.services;

import com.pteriscope.webservice.entities.review.domain.model.entity.Review;

import java.util.List;

public interface ReviewService {
    Review createReview(Long patientId, String imageBase64) throws Exception;
    Review getReview(Long id);
    List<Review> getAllReviewsFromPatient(Long patientId);
    Review getLatestReviewFromPatient(Long patientId);
    void deleteReview(Long reviewId);
}
