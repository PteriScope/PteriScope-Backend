package com.pteriscope.pteriscopebackend.patient.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PatientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private Integer age;
    private String email;
    private String lastReviewResult;
    private LocalDateTime lastReviewDate;
    private Long specialistId;

    public void setLastReviewResult(String lastReviewResult) {
        this.lastReviewResult = lastReviewResult;
    }

    public void setLastReviewDate(LocalDateTime lastReviewDate) {
        this.lastReviewDate = lastReviewDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSpecialistId(Long specialistId) {
        this.specialistId = specialistId;
    }
}
