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

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/specialists")
@CrossOrigin
public class SpecialistController {
    @Autowired
    private SpecialistService specialistService;

    @PostMapping("/register")
    public CompletableFuture<ResponseEntity<String>> registerSpecialist(@RequestBody RegisterUser newUser) {
        return specialistService.registerSpecialist(newUser)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<JwtDto>> login(@Valid @RequestBody LoginUser loginUser) {
        return specialistService.login(loginUser)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/get/{specialistId}")
    public CompletableFuture<ResponseEntity<Specialist>> getSpecialist(@PathVariable Long specialistId) {
        return specialistService.getSpecialist(specialistId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/update/{specialistId}")
    public CompletableFuture<ResponseEntity<Specialist>> updateSpecialist(@PathVariable Long specialistId, @RequestBody Specialist updatedSpecialist) {
        return specialistService.updateSpecialist(updatedSpecialist, specialistId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.badRequest().build());
    }
}
