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

import java.util.Map;

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

    @GetMapping("/{specialistId}")
    public ResponseEntity<Specialist> getSpecialist(@PathVariable Long specialistId) {
        return ResponseEntity.ok(specialistService.getSpecialist(specialistId));
    }

    @PutMapping("/{specialistId}")
    public ResponseEntity<Specialist> updateSpecialist(@PathVariable Long specialistId, @RequestBody RegisterUser updatedSpecialist) {
        return ResponseEntity.ok(specialistService.updateSpecialist(updatedSpecialist, specialistId));
    }

    @GetMapping("/{specialistId}/checkShowAdvice")
    public ResponseEntity<Boolean> checkShowAdvice(@PathVariable Long specialistId) {
        return ResponseEntity.ok(specialistService.checkShowAdviceValue(specialistId));
    }

    @PutMapping("/{specialistId}/markDoNotShowAdvice")
    public ResponseEntity<Void> markDoNotShowAdvice(@PathVariable Long specialistId) {
        specialistService.markDoNotShowAdvice(specialistId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{specialistId}/validateCurrentPassword")
    public ResponseEntity<Boolean> validateCurrentPassword(@PathVariable Long specialistId, @RequestBody Map<String, String> password) {
        String currentPassword = password.get("currentPassword");
        return ResponseEntity.ok(specialistService.validateCurrentPassword(specialistId, currentPassword));
    }
}
