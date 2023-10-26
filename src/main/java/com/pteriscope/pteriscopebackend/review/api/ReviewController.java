package com.pteriscope.pteriscopebackend.review.api;

import com.pteriscope.pteriscopebackend.review.domain.model.entity.Review;
import com.pteriscope.pteriscopebackend.review.domain.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody String imageBase64, @RequestParam Long patientId) {
        return ResponseEntity.ok(reviewService.createReview(patientId, imageBase64));
    }
}
