package com.pteriscope.pteriscopebackend.specialist.api;

import com.pteriscope.pteriscopebackend.specialist.domain.model.entity.Specialist;
import com.pteriscope.pteriscopebackend.specialist.domain.services.SpecialistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/specialists")
public class SpecialistController {
    @Autowired
    private SpecialistService specialistService;

    @PostMapping("/register")
    public ResponseEntity<Specialist> registerSpecialist(@RequestBody Specialist specialist) {
        return ResponseEntity.ok(specialistService.registerSpecialist(specialist));
    }

    @PostMapping("/login")
    public ResponseEntity<Specialist> login(@RequestBody String dni, @RequestBody String password) {
        return ResponseEntity.ok(specialistService.login(dni, password));
    }

    @GetMapping("/{specialistId}")
    public ResponseEntity<Specialist> getSpecialist(@PathVariable Long specialistId) {
        return ResponseEntity.ok(specialistService.getSpecialist(specialistId));
    }

    @PutMapping("/{specialistId}")
    public ResponseEntity<Specialist> updateSpecialist(@PathVariable Long specialistId, @RequestBody Specialist updatedSpecialist) {
        return ResponseEntity.ok(specialistService.updateSpecialist(updatedSpecialist, specialistId));
    }
}
