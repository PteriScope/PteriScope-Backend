package com.pteriscope.webservice.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginUser {
    @NotBlank(message = "DNI obligatorio")
    private String dni;
    @NotBlank(message = "Contraseña obligatoria")
    private String password;

    public void setDNI(String dni) {
        this.dni = dni;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}