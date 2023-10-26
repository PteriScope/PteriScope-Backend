package com.pteriscope.pteriscopebackend.patient.api;

import com.pteriscope.pteriscopebackend.patient.domain.model.entity.Patient;
import com.pteriscope.pteriscopebackend.patient.domain.serivces.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient, @RequestParam Long specialistId) {
        return ResponseEntity.ok(patientService.createPatient(specialistId, patient));
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<Patient> getPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.getPatient(patientId));
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long patientId, @RequestBody Patient updatedPatient) {
        return ResponseEntity.ok(patientService.updatePatient(patientId, updatedPatient));
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long patientId) {
        patientService.deletePatient(patientId);
        return ResponseEntity.ok().build();
    }
}
