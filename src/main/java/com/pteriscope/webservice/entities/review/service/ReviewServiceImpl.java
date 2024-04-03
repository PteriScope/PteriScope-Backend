package com.pteriscope.webservice.entities.review.service;

import com.pteriscope.webservice.exception.CustomException;
import com.pteriscope.webservice.entities.patient.domain.model.entity.Patient;
import com.pteriscope.webservice.entities.patient.domain.persistence.PatientRepository;
import com.pteriscope.webservice.entities.review.domain.model.entity.Review;
import com.pteriscope.webservice.entities.review.domain.persistence.ReviewRepository;
import com.pteriscope.webservice.entities.review.domain.services.ReviewService;
import com.pteriscope.webservice.util.PterygiumClass;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
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
    public Review createReview(Long patientId, String imageBase64) throws Exception {
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isPresent()) {
            Review review = new Review();

            // Process image with ECS
            String reviewResult = analyzeImage(imageBase64);
            String prediction = castPredictionToClass(reviewResult);

            // Resize image
            String resizedImage = resizeImage(imageBase64);
            log.info(String.format("resized Image: %s", resizedImage));

            // Create review info
            review.setImageBase64(resizedImage);
            review.setPatient(patient.get());
            review.setReviewDate(LocalDateTime.now());
            review.setReviewResult(prediction);
            Review savedReview = reviewRepository.save(review);

            // Update patient last review info
            patient.get().setLastReviewResult(review.getReviewResult());
            patient.get().setLastReviewDate(review.getReviewDate());
            patientRepository.save(patient.get());

            return savedReview;
        }
        else{
            throw new CustomException(HttpStatus.BAD_REQUEST, "Patient with ID " + patientId + " does not exist");
        }
    }

    String resizeImage(String base64Image) throws IOException {
        BufferedImage originalImage = decodeBase64Image(base64Image);
        int newWidth = 240;
        int newHeight = 135;
        BufferedImage resizedImage = resizeBufferedImage(originalImage, newWidth, newHeight);

        return encodeImageToBase64(resizedImage);
    }

    private BufferedImage decodeBase64Image(String base64Image) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        return ImageIO.read(new ByteArrayInputStream(imageBytes));
    }

    private BufferedImage resizeBufferedImage(BufferedImage originalImage, int newWidth, int newHeight) {
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();
        return resizedImage;
    }

    private String encodeImageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
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

    // TODO: Update class order according the retrained model
    private String castPredictionToClass(String reviewResult){
        return switch (reviewResult) {
            case "0" -> PterygiumClass.SEVERE_PTERYGIUM;
            case "1" -> PterygiumClass.MILD_PTERYGIUM;
            case "2" -> PterygiumClass.NORMAL;
            default -> reviewResult;
        };
    }

    @Override
    public Review getReview(Long reviewId) {
        log.info("==================================================================");
        log.info(String.format("reviewId: %s", reviewId));
        Review storedPatient = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Patient not found"));

        log.info("==================================================================");
        log.info(String.format("Response: %s", reviewId));
        return storedPatient;
    }

    @Transactional
    @Override
    public List<Review> getAllReviewsFromPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Patient not found"));
        return reviewRepository.getReviewsByPatientOrderByReviewDateDesc(patient);
    }

    @Transactional
    @Override
    public Review getLatestReviewFromPatient(Long patientId){
        Patient storedPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Patient not found"));


        Optional<Review> latestReview = reviewRepository.findFirstByPatientOrderByReviewDateDesc(storedPatient);

        if (latestReview.isPresent()){
            return latestReview.get();
        }
        else{
            Review nullReview = new Review();
            nullReview.setId(null);
            nullReview.setImageBase64(null);
            nullReview.setReviewResult(null);
            nullReview.setReviewDate(null);
            nullReview.setPatient(null);
            return nullReview;
        }
    }
}

