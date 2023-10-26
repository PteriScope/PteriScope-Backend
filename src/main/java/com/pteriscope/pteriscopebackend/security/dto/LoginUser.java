package com.pteriscope.pteriscopebackend.security.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginUser {
    @NotBlank(message = "DNI obligatorio")
    private String dni;
    @NotBlank(message = "Contraseña obligatoria")
    private String password;

    public String getDNI() {
        return dni;
    }

    public void setDNI(String dni) {
        this.dni = dni;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}