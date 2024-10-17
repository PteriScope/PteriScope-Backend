package com.pteriscope.webservice.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginUser {
    @NotBlank(message = "DNI obligatorio")
    private String dni;

    @NotBlank(message = "Contraseña obligatoria")
    private String password;
}