package com.pteriscope.pteriscopebackend.specialist.dto;

import lombok.Getter;

@Getter
public class SpecialistResponse {
    private Long id;
    private String name;
    private String dni;
    private String hospital;
    private String position;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
