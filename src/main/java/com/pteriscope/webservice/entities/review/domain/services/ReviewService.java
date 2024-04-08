package com.pteriscope.webservice.entities.review.domain.services;

import com.pteriscope.webservice.entities.review.domain.model.entity.Review;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ReviewService {
    CompletableFuture<Review> createReview(Long patientId, String imageBase64) throws Exception;
    CompletableFuture<Review> getReview(Long id);
    List<Review> getAllReviewsFromPatient(Long patientId);
    Review getLatestReviewFromPatient(Long patientId);
}
