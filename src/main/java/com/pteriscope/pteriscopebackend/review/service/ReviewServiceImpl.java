package com.pteriscope.pteriscopebackend.review.service;

import com.pteriscope.pteriscopebackend.exception.CustomException;
import com.pteriscope.pteriscopebackend.patient.domain.model.entity.Patient;
import com.pteriscope.pteriscopebackend.patient.domain.persistence.PatientRepository;
import com.pteriscope.pteriscopebackend.patient.dto.PatientResponse;
import com.pteriscope.pteriscopebackend.review.domain.model.entity.Review;
import com.pteriscope.pteriscopebackend.review.domain.persistence.ReviewRepository;
import com.pteriscope.pteriscopebackend.review.domain.services.ReviewService;
import com.pteriscope.pteriscopebackend.review.dto.ReviewResponse;
import com.pteriscope.pteriscopebackend.specialist.domain.model.entity.Specialist;
import com.pteriscope.pteriscopebackend.util.PterygiumClass;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    @Override
    public ReviewResponse createReview(Long patientId, String imageBase64) throws Exception {
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isPresent()) {
            Review review = new Review();
            review.setImageBase64(imageBase64);
            review.setPatient(patient.get());
            review.setReviewDate(LocalDateTime.now());

            // Process image with ECS
            String reviewResult = analyzeImage(imageBase64);
            String prediction = castPredictionToClass(reviewResult);
            review.setReviewResult(prediction);
            Review savedReview = reviewRepository.save(review);

            // Update patient last review info
            patient.get().setLastReviewResult(review.getReviewResult());
            patient.get().setLastReviewDate(review.getReviewDate());
            patientRepository.save(patient.get());

            return mapReview(savedReview);
        }
        else{
            throw new CustomException(HttpStatus.BAD_REQUEST, "Patient with ID " + patientId + " does not exist");
        }
    }

    private String analyzeImage(String base64Image) throws Exception {
        // Crear la solicitud HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(new JSONObject().put("image", base64Image).toString(), headers);

        // Realizar la solicitud POST a la API Gateway
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(apiGatewayUrl + "/predict", request, String.class);

        // Procesar la respuesta
        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject responseBody = new JSONObject(response.getBody());
            int prediction = responseBody.getInt("prediction");
            return Integer.toString(prediction);  // Convierte la predicción a String y retornala
        } else {
            throw new Exception("Error al analizar la imagen: " + response.getStatusCode());
        }
    }

    private String castPredictionToClass(String reviewResult){
        return switch (reviewResult) {
            case "0" -> PterygiumClass.SEVERE_PTERYGIUM;
            case "1" -> PterygiumClass.MILD_PTERYGIUM;
            case "2" -> PterygiumClass.NORMAL;
            default -> reviewResult;
        };
    }

    @Override
    public ReviewResponse getReview(Long reviewId) {
        log.info("==================================================================");
        log.info(String.format("reviewId: %s", reviewId));
        Review storedPatient = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Patient not found"));

        log.info("==================================================================");
        log.info(String.format("Response: %s", reviewId));
        return mapReview(storedPatient);
    }

    @Transactional
    @Override
    public List<ReviewResponse> getAllReviewsFromPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Patient not found"));
        List<Review> reviews = reviewRepository.getReviewsByPatientOrderByReviewDateDesc(patient);
        return reviews.stream()
                .map(this::mapReview)
                .toList();
    }

    private ReviewResponse mapReview(Review review){
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setImageBase64(review.getImageBase64());
        response.setReviewResult(review.getReviewResult());
        response.setReviewDate(review.getReviewDate());
        response.setPatientId(review.getPatient().getId());
        return response;
    }

    @Transactional
    @Override
    public ReviewResponse getLatestReviewFromPatient(Long patientId){
        Patient storedPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Patient not found"));


        Optional<Review> latestReview = reviewRepository.findFirstByPatientOrderByReviewDateDesc(storedPatient);

        if (latestReview.isPresent()){
            return mapReview(latestReview.get());
        }
        else{
            ReviewResponse reviewResponse = new ReviewResponse();
            reviewResponse.setId(null);
            reviewResponse.setImageBase64(null);
            reviewResponse.setReviewResult(null);
            reviewResponse.setReviewDate(null);
            reviewResponse.setPatientId(null);
            return reviewResponse;
        }
    }
}

