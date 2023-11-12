package com.pteriscope.pteriscopebackend.security.dto;

public class JwtDto {
    private Long id;
    private String token;

    public JwtDto() {
    }

    public JwtDto(String token, Long id) {
        this.token = token;
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public Long getId() {
        return id;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
