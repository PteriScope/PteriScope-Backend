package com.pteriscope.webservice.entities.patient.api;

import com.pteriscope.webservice.entities.patient.domain.model.entity.Patient;
import com.pteriscope.webservice.entities.patient.domain.serivces.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/specialists/{specialistId}/createPatient")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient, @PathVariable Long specialistId) {
        return ResponseEntity.ok(patientService.createPatient(specialistId, patient));
    }

    @GetMapping("/patients/{patientId}")
    public ResponseEntity<Patient> getPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.getPatient(patientId));
    }

    @GetMapping("/specialists/{specialistId}/patients")
    public ResponseEntity<List<Patient>> getPatientFromSpecialist(@PathVariable Long specialistId) {
        return ResponseEntity.ok(patientService.getPatientFromSpecialist(specialistId));
    }

    @PutMapping("/patients/{patientId}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long patientId, @RequestBody Patient updatedPatient) {
        return ResponseEntity.ok(patientService.updatePatient(patientId, updatedPatient));
    }

    @DeleteMapping("/patients/{patientId}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long patientId) {
        patientService.deletePatient(patientId);
        return ResponseEntity.ok().build();
    }
}
