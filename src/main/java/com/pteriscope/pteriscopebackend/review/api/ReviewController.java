package com.pteriscope.pteriscopebackend.review.api;

import com.pteriscope.pteriscopebackend.patient.dto.PatientResponse;
import com.pteriscope.pteriscopebackend.review.domain.model.entity.Review;
import com.pteriscope.pteriscopebackend.review.domain.services.ReviewService;
import com.pteriscope.pteriscopebackend.review.dto.ReviewResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@RequestBody Map<String, String> requestBody, @RequestParam Long patientId) throws Exception {
        String imageBase64 = requestBody.get("imageBase64");
        log.info("==================================================================");
        log.info(String.format("Solicitud llegada a CreateReview con request: %s, y patientId: %s", imageBase64, patientId));
        return ResponseEntity.ok(reviewService.createReview(patientId, imageBase64));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getPatient(@PathVariable Long reviewId) {
        log.info("==================================================================");
        log.info(String.format("Solicitud llegada a CreateReview con reviewId: %s", reviewId));
        return ResponseEntity.ok(reviewService.getReview(reviewId));
    }
}
