package com.pteriscope.pteriscopebackend.review.dto;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponse {
    private Long id;
    private String imageBase64;
    private String reviewResult;
    private LocalDateTime reviewDate;
    private Long patientId;

    public void setId(Long id) {
        this.id = id;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public void setReviewResult(String reviewResult) {
        this.reviewResult = reviewResult;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}
