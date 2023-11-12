package com.pteriscope.pteriscopebackend.patient.dto;

import java.time.LocalDateTime;

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

    public String getLastReviewResult() {
        return lastReviewResult;
    }

    public void setLastReviewResult(String lastReviewResult) {
        this.lastReviewResult = lastReviewResult;
    }

    public LocalDateTime getLastReviewDate() {
        return lastReviewDate;
    }

    public void setLastReviewDate(LocalDateTime lastReviewDate) {
        this.lastReviewDate = lastReviewDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(Long specialistId) {
        this.specialistId = specialistId;
    }
}
