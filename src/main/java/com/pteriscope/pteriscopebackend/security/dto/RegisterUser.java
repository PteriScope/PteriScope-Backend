package com.pteriscope.pteriscopebackend.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

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
    public Set<String> roles = new HashSet<>();
}
