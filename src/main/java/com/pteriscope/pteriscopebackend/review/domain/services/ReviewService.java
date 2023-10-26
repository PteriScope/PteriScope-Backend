package com.pteriscope.pteriscopebackend.review.domain.services;

import com.pteriscope.pteriscopebackend.review.domain.model.entity.Review;

public interface ReviewService {
    public Review createReview(Long patientId, String imageBase64);
}
