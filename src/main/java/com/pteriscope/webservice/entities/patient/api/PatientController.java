package com.pteriscope.webservice.entities.patient.api;

import com.pteriscope.webservice.entities.patient.domain.model.entity.Patient;
import com.pteriscope.webservice.entities.patient.domain.serivces.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/specialists/createPatient/{specialistId}")
    public CompletableFuture<ResponseEntity<Patient>> createPatient(@RequestBody Patient patient, @PathVariable Long specialistId) {
        return patientService.createPatient(specialistId, patient)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/patients/get/{patientId}")
    public CompletableFuture<ResponseEntity<Patient>> getPatient(@PathVariable Long patientId) {
        return patientService.getPatient(patientId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/specialists/{specialistId}/patients")
    public CompletableFuture<ResponseEntity<List<Patient>>> getPatientFromSpecialist(@PathVariable Long specialistId) {
        return patientService.getPatientFromSpecialist(specialistId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/patients/update/{patientId}")
    public CompletableFuture<ResponseEntity<Patient>> updatePatient(@PathVariable Long patientId, @RequestBody Patient updatedPatient) {
        return patientService.updatePatient(patientId, updatedPatient)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/patients/delete/{patientId}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long patientId) {
        patientService.deletePatient(patientId);
        return ResponseEntity.ok().build();
    }
}
