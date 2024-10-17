package com.pteriscope.webservice.security.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtDto {
    private final Long id;
    private String token;

    public JwtDto(String token, Long id) {
        this.token = token;
        this.id = id;
    }
}
