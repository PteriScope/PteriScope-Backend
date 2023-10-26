package com.pteriscope.pteriscopebackend.review.service;

import com.pteriscope.pteriscopebackend.exception.CustomException;
import com.pteriscope.pteriscopebackend.patient.domain.model.entity.Patient;
import com.pteriscope.pteriscopebackend.patient.domain.persistence.PatientRepository;
import com.pteriscope.pteriscopebackend.review.domain.model.entity.Review;
import com.pteriscope.pteriscopebackend.review.domain.persistence.ReviewRepository;
import com.pteriscope.pteriscopebackend.review.domain.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private SageMakerService sageMakerService;

    @Override
    public Review createReview(Long patientId, String imageBase64) {
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isPresent()) {
            Review review = new Review();
            review.setImageBase64(imageBase64);
            review.setPatient(patient.get());
            review.setReviewDate(LocalDate.now());

            // Process image with SageMaker
            String reviewResult = sageMakerService.processImage(imageBase64);
            review.setReviewResult(reviewResult);

            return reviewRepository.save(review);
        }
        else{
            throw new CustomException(HttpStatus.BAD_REQUEST, "Patient with ID " + patientId + " does not exist");
        }
    }
}

