package com.pteriscope.pteriscopebackend.review.domain.services;

import com.pteriscope.pteriscopebackend.review.dto.ReviewResponse;

public interface ReviewService {
    public ReviewResponse createReview(Long patientId, String imageBase64) throws Exception;
    public ReviewResponse getReview(Long id);
}
