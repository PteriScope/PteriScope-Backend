package com.pteriscope.webservice.entities.review.api;

import com.pteriscope.webservice.entities.review.domain.model.entity.Review;
import com.pteriscope.webservice.entities.review.domain.services.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/reviews")
    public CompletableFuture<ResponseEntity<Review>> createReview(@RequestBody Map<String, String> requestBody, @RequestParam Long patientId) throws Exception {
        String imageBase64 = requestBody.get("imageBase64");
        return reviewService.createReview(patientId, imageBase64)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/reviews/{reviewId}")
    public CompletableFuture<ResponseEntity<Review>> getReview(@PathVariable Long reviewId) {
        return reviewService.getReview(reviewId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.badRequest().build());
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
