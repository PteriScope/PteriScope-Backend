package com.pteriscope.pteriscopebackend.review.api;

import com.pteriscope.pteriscopebackend.review.domain.services.ReviewService;
import com.pteriscope.pteriscopebackend.review.dto.ReviewResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponse> createReview(@RequestBody Map<String, String> requestBody, @RequestParam Long patientId) throws Exception {
        String imageBase64 = requestBody.get("imageBase64");
        return ResponseEntity.ok(reviewService.createReview(patientId, imageBase64));
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReview(reviewId));
    }

    @GetMapping("/patients/{patientId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getAllReviewsFromPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(reviewService.getAllReviewsFromPatient(patientId));
    }

    @GetMapping("/patients/{patientId}/latest_reviews")
    public ResponseEntity<ReviewResponse> getLatestReviewFromPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(reviewService.getLatestReviewFromPatient(patientId));
    }
}
