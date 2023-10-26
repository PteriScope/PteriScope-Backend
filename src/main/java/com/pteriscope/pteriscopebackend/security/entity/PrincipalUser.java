package com.pteriscope.pteriscopebackend.security.entity;

import com.pteriscope.pteriscopebackend.specialist.domain.model.entity.Specialist;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PrincipalUser implements UserDetails {
    private String name;
    private String dni;
    private String password;
    private String hospital;
    private String position;
    private Collection<? extends GrantedAuthority> authorities;

    public PrincipalUser(String name, String dni, String password, String hospital, String position, Collection<? extends GrantedAuthority> authorities) {
        this.name = name;
        this.dni = dni;
        this.password = password;
        this.hospital = hospital;
        this.position = position;
        this.authorities = authorities;
    }

    public static PrincipalUser build(Specialist usuario) {
        List<GrantedAuthority> authorities =
                usuario.getRoles().stream().map(rol -> new SimpleGrantedAuthority(rol
                        .getRolName().name())).collect(Collectors.toList());
        return new PrincipalUser(usuario.getName(), usuario.getDni(), usuario.getPassword(), usuario.getHospital(), usuario.getPosition(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return dni;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getName() {
        return name;
    }

    public String getDni() {
        return dni;
    }
}

