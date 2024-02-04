package com.pteriscope.webservice.entities.hospital.api;

import com.pteriscope.webservice.entities.hospital.domain.model.entity.Hospital;
import com.pteriscope.webservice.entities.hospital.domain.services.HospitalService;
import com.pteriscope.webservice.security.dto.JwtDto;
import com.pteriscope.webservice.security.dto.LoginUser;
import com.pteriscope.webservice.security.dto.RegisterUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hospitals")
@CrossOrigin
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    @PostMapping("/register")
    public ResponseEntity<String> registerHospital(@RequestBody RegisterUser newHospital) {
        return ResponseEntity.ok(hospitalService.registerHospital(newHospital));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUser loginUser) {
        return ResponseEntity.ok(hospitalService.login(loginUser));
    }

    @GetMapping("/get/{hospitalId}")
    public ResponseEntity<Hospital> getHospital(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(hospitalService.getHospital(hospitalId));
    }

    @PutMapping("/update/{hospitalId}")
    public ResponseEntity<Hospital> updateHospital(@PathVariable Long hospitalId, @RequestBody Hospital updatedHospital) {
        return ResponseEntity.ok(hospitalService.updateHospital(updatedHospital, hospitalId));
    }
}
