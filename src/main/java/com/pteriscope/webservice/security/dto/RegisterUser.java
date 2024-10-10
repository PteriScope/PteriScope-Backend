package com.pteriscope.webservice.security.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterUser {
    @NotBlank(message = "Nombre obligatorio")
    public String name;
    @NotBlank(message = "DNI obligatorio")
    public String dni;
    @NotBlank(message = "Contraseña obligatoria")
    public String password;
    @NotBlank(message = "Nombre de hostipal obligatorio")
    public String hospital;
    @NotBlank(message = "Nombre del cargo obligatorio")
    public String position;
}
