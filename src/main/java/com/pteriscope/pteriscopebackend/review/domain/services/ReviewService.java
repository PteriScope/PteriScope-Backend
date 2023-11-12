package com.pteriscope.pteriscopebackend.review.domain.services;

import com.pteriscope.pteriscopebackend.review.dto.ReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewResponse createReview(Long patientId, String imageBase64) throws Exception;
    ReviewResponse getReview(Long id);
    List<ReviewResponse> getAllReviewsFromPatient(Long patientId);
    ReviewResponse getLatestReviewFromPatient(Long patientId);

}
