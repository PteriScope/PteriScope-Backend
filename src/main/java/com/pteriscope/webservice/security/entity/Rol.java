package com.pteriscope.webservice.security.entity;

import com.pteriscope.webservice.security.enums.RolName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;


@Getter
@Entity
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RolName rolName;

    public Rol() {
    }

    public Rol(@NotNull RolName rolName) {
        this.rolName = rolName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRolName(RolName rolName) {
        this.rolName = rolName;
    }
}