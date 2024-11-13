package com.pteriscope.webservice.security.entity;

import com.pteriscope.webservice.security.enums.RolName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RolName rolName;
}