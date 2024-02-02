package com.pteriscope.webservice.entities.review.api;

import com.pteriscope.webservice.entities.review.domain.model.entity.Review;
import com.pteriscope.webservice.entities.review.domain.services.ReviewService;
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
    public ResponseEntity<Review> createReview(@RequestBody Map<String, String> requestBody, @RequestParam Long patientId) throws Exception {
        String imageBase64 = requestBody.get("imageBase64");
        return ResponseEntity.ok(reviewService.createReview(patientId, imageBase64));
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReview(reviewId));
    }

    @GetMapping("/patients/{patientId}/reviews")
    public ResponseEntity<List<Review>> getAllReviewsFromPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(reviewService.getAllReviewsFromPatient(patientId));
    }

    @GetMapping("/patients/{patientId}/latest_reviews")
    public ResponseEntity<Review> getLatestReviewFromPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(reviewService.getLatestReviewFromPatient(patientId));
    }
}
