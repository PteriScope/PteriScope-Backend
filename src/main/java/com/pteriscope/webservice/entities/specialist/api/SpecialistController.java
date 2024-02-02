package com.pteriscope.webservice.entities.specialist.api;

import com.pteriscope.webservice.entities.specialist.domain.services.SpecialistService;
import com.pteriscope.webservice.security.dto.JwtDto;
import com.pteriscope.webservice.security.dto.LoginUser;
import com.pteriscope.webservice.security.dto.RegisterUser;
import com.pteriscope.webservice.entities.specialist.domain.model.entity.Specialist;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/specialists")
@CrossOrigin
public class SpecialistController {
    @Autowired
    private SpecialistService specialistService;

    @PostMapping("/register")
    public ResponseEntity<String> registerSpecialist(@RequestBody RegisterUser newUser) {
        return ResponseEntity.ok(specialistService.registerSpecialist(newUser));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUser loginUser) {
        return ResponseEntity.ok(specialistService.login(loginUser));
    }

    @GetMapping("/get/{specialistId}")
    public ResponseEntity<Specialist> getSpecialist(@PathVariable Long specialistId) {
        return ResponseEntity.ok(specialistService.getSpecialist(specialistId));
    }

    @PutMapping("/update/{specialistId}")
    public ResponseEntity<Specialist> updateSpecialist(@PathVariable Long specialistId, @RequestBody Specialist updatedSpecialist) {
        return ResponseEntity.ok(specialistService.updateSpecialist(updatedSpecialist, specialistId));
    }
}
