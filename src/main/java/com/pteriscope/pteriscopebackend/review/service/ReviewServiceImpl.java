package com.pteriscope.pteriscopebackend.review.service;

import com.pteriscope.pteriscopebackend.exception.CustomException;
import com.pteriscope.pteriscopebackend.patient.domain.model.entity.Patient;
import com.pteriscope.pteriscopebackend.patient.domain.persistence.PatientRepository;
import com.pteriscope.pteriscopebackend.patient.dto.PatientResponse;
import com.pteriscope.pteriscopebackend.review.domain.model.entity.Review;
import com.pteriscope.pteriscopebackend.review.domain.persistence.ReviewRepository;
import com.pteriscope.pteriscopebackend.review.domain.services.ReviewService;
import com.pteriscope.pteriscopebackend.review.dto.ReviewResponse;
import com.pteriscope.pteriscopebackend.util.PterygiumClass;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

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
            review.setReviewDate(LocalDate.now());

            // Process image with ECS
            String reviewResult = analyzeImage(imageBase64);
            String prediction = castPredictionToClass(reviewResult);
            review.setReviewResult(prediction);
            Review savedReview = reviewRepository.save(review);

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
        Review storedPatient = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Patient not found"));

        return mapReview(storedPatient);
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
}

