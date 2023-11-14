package com.pteriscope.pteriscopebackend.security.dto;

import lombok.Getter;

@Getter
public class JwtDto {
    private final Long id;
    private String token;

    public JwtDto(String token, Long id) {
        this.token = token;
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
