package com.pteriscope.webservice.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUser {
    @NotBlank(message = "Nombre obligatorio")
    private String name;
    @NotBlank(message = "DNI obligatorio")
    private String dni;
    @NotBlank(message = "Contraseña obligatoria")
    private String password;
    @NotBlank(message = "Nombre de hostipal obligatorio")
    private String hospital;
    @NotBlank(message = "Nombre del cargo obligatorio")
    private String position;
}
